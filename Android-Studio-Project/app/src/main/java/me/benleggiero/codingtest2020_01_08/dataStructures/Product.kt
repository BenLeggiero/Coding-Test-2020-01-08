package me.benleggiero.codingtest2020_01_08.dataStructures

import android.net.Uri
import me.benleggiero.codingtest2020_01_08.conveniences.enforcingHttps
import me.benleggiero.codingtest2020_01_08.serialization.ProductJson
import java.net.URL

data class Product(
    val title: String,
    val author: String?,
    val image: Image
) {
    constructor(json: ProductJson): this(
        title = json.title,
        author = json.authorName,
        image = json.imageUriString?.let { Image.url(URL(it.enforcingHttps)) } ?: Image.none
    )



    companion object {
        val loading: Product
            get() = Product(
                title = "Loading...",
                author = "Please wait",
                image = Image.loading
            )
    }



    sealed class Image {
        object none: Image()
        object loading: Image()
        class url(val url: URL): Image()
    }
}
