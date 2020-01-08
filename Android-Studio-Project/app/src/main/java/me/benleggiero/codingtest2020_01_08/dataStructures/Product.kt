package me.benleggiero.codingtest2020_01_08.dataStructures

import android.net.Uri
import android.widget.ImageView
import me.benleggiero.codingtest2020_01_08.serialization.ProductJson

data class Product(
    val title: String,
    val author: String?,
    val imageUri: Uri?
) {
    constructor(json: ProductJson): this(
        title = json.title,
        author = json.author,
        imageUri = json.imageUriString.let { Uri.parse(it) }
    )
}
