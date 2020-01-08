package me.benleggiero.codingtest2020_01_08.controllers

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.benleggiero.codingtest2020_01_08.dataStructures.Product
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import me.benleggiero.codingtest2020_01_08.*
import me.benleggiero.codingtest2020_01_08.dataStructures.Product.Image.*


class ProductsRecyclerViewAdapter(
    val products: List<Product>
)
    : RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_product, parent, false))

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]

        holder.titleTextView.text = product.title
        holder.authorTextView.text = product.author ?: ""

        when (product.image) {
            is none -> holder.imageView.setImageResource(R.drawable.ic_broken_image_black_24dp)
            is loading -> holder.imageView.setImageResource(R.drawable.ic_cached_black_24dp)
            is uri -> holder.imageView.setImageURI(product.image.uri)
        }
    }




    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById(R.id.productImageView) as ImageView
        val titleTextView = itemView.findViewById(R.id.productTitle) as TextView
        val authorTextView = itemView.findViewById(R.id.productAuthorName) as TextView
    }
}