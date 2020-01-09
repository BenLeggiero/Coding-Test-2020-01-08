package me.benleggiero.codingtest2020_01_08.controllers

import android.app.*
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.*
import me.benleggiero.codingtest2020_01_08.*
import me.benleggiero.codingtest2020_01_08.controllers.ProductsRecyclerViewAdapter.Products.*
import me.benleggiero.codingtest2020_01_08.conveniences.*
import me.benleggiero.codingtest2020_01_08.dataStructures.*
import me.benleggiero.codingtest2020_01_08.dataStructures.Product.Image.*
import me.benleggiero.codingtest2020_01_08.serialization.*
import java.lang.Integer.*
import kotlin.properties.*


class ProductsRecyclerViewAdapter(
    val context: Activity,
    products: Products
)
    : RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder>()
{

    private var products by Delegates.observable(initialValue = products) { _, old, new ->
        context.runOnUiThread {
//            this.notifyDataSetChanged()
            val oldCount = old.numberOfProductsLoadedSoFar
            val newCount = new.numberOfProductsLoadedSoFar
            val countDelta = newCount - oldCount

            when {
                countDelta == 0 -> return@runOnUiThread
                countDelta > 0 -> this.notifyItemRangeInserted(oldCount, countDelta)
                else -> this.notifyItemRangeRemoved(newCount, -countDelta)
            }
        }
    }

//    var shownProductCount by Delegates.observable(initialValue = 0) { _, _, _ ->
//        this.notifyDataSetChanged()
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_product, parent, false))


    override fun getItemCount() = products.numberOfProductsLoadedSoFar// min(100, products.size)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position] ?: return

        holder.titleTextView.text = product.title
        holder.authorTextView.text = product.author ?: ""

        when (product.image) {
            is none -> holder.imageView.setImageResource(R.drawable.ic_broken_image_black_24dp)
            is loading -> holder.imageView.setImageResource(R.drawable.ic_cached_black_24dp)
            is url -> {

                holder.imageView.setImageResource(R.drawable.ic_cached_black_24dp)

                async(product.image.url, backgroundFunction = ::Bitmap) { bitmap ->
                    context.runOnUiThread {
                        holder.imageView.setImageBitmap(bitmap)
                    }
                }

//                ImageFetchTask(imageUrl = product.image.url, onImageReady = { bitmap ->
//                    context.runOnUiThread {
//                        holder.imageView.setImageBitmap(bitmap)
//                    }
//                }).execute()
            }
        }
    }



    // MARK: - Conveniences

    fun asyncLoadFirstProducts(amountToLoad: Int, loader: ProductsLoader) {
        async {
            this.products = entirelyCached(emptyList())
            this.products = Products.loading

            val newProducts = loadingFromInputStream(loader)
            newProducts.loadNextProducts(amountToLoad)
            this.products = products
        }
    }


    fun asyncLoadNextProducts(amountToLoad: Int) {
        async { loadNextProducts(amountToLoad) }
    }


    private fun loadNextProducts(amountToLoad: Int) {
        val oldCount = this.itemCount
        products.loadNextProducts(amountToLoad)
        context.runOnUiThread {
            this.notifyItemRangeInserted(oldCount, amountToLoad)
        }
    }




    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById(R.id.productImageView) as ImageView
        val titleTextView = itemView.findViewById(R.id.productTitle) as TextView
        val authorTextView = itemView.findViewById(R.id.productAuthorName) as TextView
    }



    sealed class Products {
        class entirelyCached(val allProducts: List<Product>): Products()
        class loadingFromInputStream(val loader: ProductsLoader, var loadedProducts: MutableList<Product> = mutableListOf()): Products()

        companion object {
            val loading get() = entirelyCached(listOf(Product.loading))
        }
    }
}


// MARK: - ProductsRecyclerViewAdapter.Products conveniences

private fun ProductsRecyclerViewAdapter.Products.loadNextProducts(amountToLoad: Int) {
    when (this) {
        is entirelyCached -> return
        is loadingFromInputStream -> encache(loader.nextProductJsons(maxAmountToRead = amountToLoad).map(::Product))
    }
}


operator fun ProductsRecyclerViewAdapter.Products.get(index: Int): Product? =
    when (this) {
        is entirelyCached -> allProducts.elementAtOrNull(index)
        is loadingFromInputStream -> getOrLoadProduct(index = index)
    }

private fun loadingFromInputStream.getOrLoadProduct(index: Int): Product? {
    if (index < loadedProducts.size) {
        return loadedProducts[index]
    }
    else {
        this.loadNextProducts(index - (loadedProducts.size - 1))

        return if (index < loadedProducts.size) {
            loadedProducts[index]
        } else {
            null
        }
    }
}


private fun loadingFromInputStream.encache(newProducts: List<Product>) {
    loadedProducts.addAll(newProducts)
}


val ProductsRecyclerViewAdapter.Products.productsLoadedSoFar: List<Product> get() = when (this) {
    is entirelyCached -> allProducts
    is loadingFromInputStream -> loadedProducts
}


val ProductsRecyclerViewAdapter.Products.numberOfProductsLoadedSoFar get() = productsLoadedSoFar.size