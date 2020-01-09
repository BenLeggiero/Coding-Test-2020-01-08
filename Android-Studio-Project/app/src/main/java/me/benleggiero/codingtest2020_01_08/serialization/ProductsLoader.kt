package me.benleggiero.codingtest2020_01_08.serialization

import android.util.JsonReader
import me.benleggiero.codingtest2020_01_08.*
import me.benleggiero.codingtest2020_01_08.conveniences.compactMapNextArray
import me.benleggiero.codingtest2020_01_08.conveniences.readJson
import me.benleggiero.codingtest2020_01_08.conveniences.readObject
import me.benleggiero.codingtest2020_01_08.dataStructures.Product
import java.io.InputStream

object ProductsLoader {
//    fun loadProducts(resourceId: Int = R.raw.products) =
//        loadProductsJson(resourceId = resourceId).map(::Product)


    fun loadProducts(inputStream: InputStream) =
        loadProductsJson(inputStream = inputStream).map(::Product)


//    fun loadProductsJson(resourceId: Int = R.raw.products) =
//        readJson(resourceId = resourceId) { jsonReader ->
//            jsonReader.productsList() ?: emptyList()
//        }


    fun loadProductsJson(inputStream: InputStream) =
        readJson(inputStream = inputStream) { jsonReader ->
            jsonReader.productsList() ?: emptyList()
        }
}



private fun JsonReader.productsList(): List<ProductJson>? {
    return if (!hasNext()) {
        null
    }
    else {
        compactMapNextArray(JsonReader::nextProduct)
    }
}


private fun JsonReader.nextProduct(): ProductJson? =
    readObject {
        ProductJson(it)
    }
