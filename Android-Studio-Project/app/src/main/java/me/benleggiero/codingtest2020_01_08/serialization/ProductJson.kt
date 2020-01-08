package me.benleggiero.codingtest2020_01_08.serialization

import org.json.JSONObject

data class ProductJson(
    val title: String,
    val author: String?,
    val imageUriString: String?
) {

    companion object {

        private const val titleJsonKey = """title"""
        private const val authorJsonKey = """author"""
        private const val imageUriStringJsonKey = """imageURL"""

        operator fun invoke(rawJsonObject: JSONObject): ProductJson? {
            return ProductJson(
                title = rawJsonObject[titleJsonKey] as? String ?: return null,
                author = rawJsonObject[authorJsonKey] as? String?,
                imageUriString = rawJsonObject[imageUriStringJsonKey] as? String?
            )

        }
    }
}