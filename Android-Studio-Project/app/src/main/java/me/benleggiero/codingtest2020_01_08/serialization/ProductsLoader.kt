package me.benleggiero.codingtest2020_01_08.serialization

import android.content.res.Resources
import android.util.JsonReader
import me.benleggiero.codingtest2020_01_08.R
import me.benleggiero.codingtest2020_01_08.conveniences.compactMapNextArray
import me.benleggiero.codingtest2020_01_08.conveniences.readJson
import me.benleggiero.codingtest2020_01_08.conveniences.readObject
import java.io.*

class ProductsLoader(inputStream: InputStream) {

    internal val jsonReader = JsonReader(InputStreamReader(inputStream))

    init {
        jsonReader.beginArray()
    }


//    fun loadProductsJson() = jsonReader.productsList() ?: emptyList()

//    fun nextProductJson() = jsonReader.nextProduct()
}



//private fun JsonReader.productsList(): List<ProductJson>? {
//    return if (!hasNext()) {
//        null
//    }
//    else {
//        compactMapNextArray(JsonReader::nextProduct)
//    }
//}


private fun JsonReader.nextProduct(): ProductJson? =
    readObject {
        ProductJson(it)
    }


fun ProductsLoader.nextProductJsons(maxAmountToRead: Int): List<ProductJson> {
    return (1..maxAmountToRead)
        .asSequence()
        .mapNotNull { jsonReader.nextProduct() }
        .takeWhile { jsonReader.hasNext() }
        .toList()
}
