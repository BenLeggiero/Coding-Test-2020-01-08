package me.benleggiero.codingtest2020_01_08.serialization

import android.util.JsonReader
import me.benleggiero.codingtest2020_01_08.*
import me.benleggiero.codingtest2020_01_08.dataStructures.Product

object ProductsLoader {
    fun loadProducts(resourceId: Int = R.raw.products) =
        loadProductsJson(resourceId = resourceId).map(::Product)


    fun loadProductsJson(resourceId: Int = R.raw.products) =
        readJson(resourceId = resourceId) { jsonReader ->
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
