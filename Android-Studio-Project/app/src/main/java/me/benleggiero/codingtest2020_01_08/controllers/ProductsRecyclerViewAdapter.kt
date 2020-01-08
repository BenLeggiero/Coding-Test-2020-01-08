package me.benleggiero.codingtest2020_01_08.controllers

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.benleggiero.codingtest2020_01_08.dataStructures.Product
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import me.benleggiero.codingtest2020_01_08.*


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

        if (null != product.imageUri) {
            holder.imageView.setImageURI(product.imageUri)
        }
        else {
            holder.imageView.setImageResource(R.drawable.ic_broken_image_black_24dp)
        }
    }




    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById(R.id.productImageView) as ImageView
        val titleTextView = itemView.findViewById(R.id.productTitle) as TextView
        val authorTextView = itemView.findViewById(R.id.productAuthorName) as TextView
    }
}