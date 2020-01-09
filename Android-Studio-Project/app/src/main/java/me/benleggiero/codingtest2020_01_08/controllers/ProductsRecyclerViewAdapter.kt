package me.benleggiero.codingtest2020_01_08.controllers

import android.app.Activity
import android.graphics.Bitmap
import android.os.AsyncTask
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.benleggiero.codingtest2020_01_08.dataStructures.Product
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import me.benleggiero.codingtest2020_01_08.*
import me.benleggiero.codingtest2020_01_08.conveniences.Bitmap
import me.benleggiero.codingtest2020_01_08.dataStructures.Product.Image.*
import java.lang.Integer.min
import java.net.URL
import kotlin.properties.Delegates


class ProductsRecyclerViewAdapter(
    val context: Activity,
    products: List<Product>,
    val onUserDidTapItem: (tappedProduct: Product) -> Unit
)
    : RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder>()
{

    var products by Delegates.observable(initialValue = products.toMutableList()) { _, _, _ ->
        this.notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_product, parent, false))


    override fun getItemCount() = min(100, products.size)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]

        holder.titleTextView.text = product.title
        holder.authorTextView.text = product.author ?: ""

        when (product.image) {
            is none -> holder.imageView.setImageResource(R.drawable.ic_broken_image_black_24dp)
            is loading -> holder.imageView.setImageResource(R.drawable.ic_cached_black_24dp)
            is url -> {
                holder.imageView.setImageResource(R.drawable.ic_cached_black_24dp)

                ImageFetchTask(imageUrl = product.image.url, onImageReady = {
                    context.runOnUiThread {
                        holder.imageView.setImageBitmap(it)
                    }
                }).execute()
            }
        }

        holder.itemView.setOnClickListener {
            onUserDidTapItem(product)
        }
    }


    fun updateProduct(updatedProduct: Product) {
        val indexToUpdate = products.indexOfFirst { it.locallyUniqueIdentifier == updatedProduct.locallyUniqueIdentifier }

        if (indexToUpdate < 0) {
            return
        }

        products[indexToUpdate] = updatedProduct
        this.notifyItemChanged(indexToUpdate)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById(R.id.productImageView) as ImageView
        val titleTextView = itemView.findViewById(R.id.productTitle) as TextView
        val authorTextView = itemView.findViewById(R.id.productAuthorName) as TextView
    }



    private class ImageFetchTask(
        val imageUrl: URL,
        val onImageReady: (image: Bitmap) -> Unit
    )
        : AsyncTask<Unit, Unit, Bitmap>()
    {
        override fun doInBackground(vararg params: Unit?): Bitmap = Bitmap(imageUrl)

        override fun onPostExecute(result: Bitmap?) {
            result?.let(onImageReady)
        }
    }
}