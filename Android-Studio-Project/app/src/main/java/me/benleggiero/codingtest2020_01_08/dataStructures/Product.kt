package me.benleggiero.codingtest2020_01_08.dataStructures

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import me.benleggiero.codingtest2020_01_08.conveniences.enforcingHttps
import me.benleggiero.codingtest2020_01_08.serialization.ProductJson
import java.net.URL

data class Product(
    val title: String,
    val author: String?,
    val image: Image,
    var isFavorited: Boolean = false
)
    : Parcelable
{

    constructor(json: ProductJson): this(
        title = json.title,
        author = json.authorName,
        image = json.imageUriString?.let { Image.url(URL(it.enforcingHttps)) } ?: Image.none
    )

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        Image(parcel) ?: Image.none.also {
            Log.e("Parcel", "Could not create image from parcel")
        },// error("Could not create image from parcel"),
        parcel.readByte() != 0.toByte()
    )






    companion object CREATOR : Parcelable.Creator<Product> {

        val loading: Product
            get() = Product(
                title = "Loading...",
                author = "Please wait",
                image = Image.loading
            )


        override fun createFromParcel(parcel: Parcel) = Product(parcel)
        override fun newArray(size: Int): Array<Product?> = arrayOfNulls(size)
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeParcelable(ImageParcel(image), 0)
        parcel.writeByte(if (isFavorited) 1 else 0)
    }


    override fun describeContents(): Int {
        return 0
    }



    sealed class Image {
        object none: Image()
        object loading: Image()
        class url(val url: URL): Image()

        companion object
    }



    class ImageParcel private constructor(
        val typeString: String,
        val detail: String?
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            typeString = parcel.readString() ?: error("Image parcel missing type string"),
            detail = parcel.readString()
        )

        constructor(image: Image): this(
            typeString = typeKeyForImage(image),
            detail = detailStringForImage(image)
        )


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(typeString)
            parcel.writeString(detail)
        }


        override fun describeContents(): Int {
            return 0
        }


        companion object CREATOR : Parcelable.Creator<ImageParcel> {

            const val noneTypeKey = """none"""
            const val loadingTypeKey = """loading"""
            const val urlTypeKey = """url"""


            fun typeKeyForImage(image: Image) = when (image) {
                is Image.none -> noneTypeKey
                is Image.loading -> loadingTypeKey
                is Image.url -> urlTypeKey
            }


            fun detailStringForImage(image: Image) = when (image) {
                is Image.none -> null
                is Image.loading -> null
                is Image.url -> image.url.toString()
            }


            override fun createFromParcel(parcel: Parcel): ImageParcel {
                return ImageParcel(parcel)
            }

            override fun newArray(size: Int): Array<ImageParcel?> {
                return arrayOfNulls(size)
            }
        }
    }


    /*: Parcelable {


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        when (this) {
            is Image.none -> parcel.writeString(CREATOR.noneKey)
            is Image.loading -> parcel.writeString("""loading""")
            is Image.url -> {
                parcel.writeString("""url""")
                parcel.writeString(url.toString())
            }
        }
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Image> {

        const val noneKey = """none"""

        override fun createFromParcel(parcel: Parcel): Image {
            when (parcel.readString()) {
                null -> error("Image parcel missing type string")

            }
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }
}*/
}



operator fun Product.Image.Companion.invoke(parcel: Product.ImageParcel): Product.Image? {
    return when (parcel.typeString) {
        Product.ImageParcel.noneTypeKey -> Product.Image.none
        Product.ImageParcel.loadingTypeKey -> Product.Image.loading
        Product.ImageParcel.urlTypeKey -> Product.Image.url(URL(parcel.detail ?: return null))
        else -> null.also { Log.e("Parcel", "Unexpected image parcel type: ${parcel.typeString}") }
    }
}


operator fun Product.Image.Companion.invoke(parcel: Parcel): Product.Image? {
    return Product.Image(
        parcel = parcel.readParcelable<Product.ImageParcel>(Product.ImageParcel::class.java.classLoader)
            ?: return null
    )
}
