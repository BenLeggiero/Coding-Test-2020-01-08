package me.benleggiero.codingtest2020_01_08.serialization

import android.util.JsonReader
import android.util.Log
import me.benleggiero.codingtest2020_01_08.conveniences.forEachNameInObject
import org.json.JSONObject

data class ProductJson(
    val title: String,
    val authorName: String?,
    val imageUriString: String?
) {

    companion object {

        private const val titleJsonKey = """title"""
        private const val authorJsonKey = """author"""
        private const val imageUriStringJsonKey = """imageURL"""

        operator fun invoke(rawJsonObject: JSONObject): ProductJson? {
            return ProductJson(
                title = rawJsonObject[titleJsonKey] as? String ?: return null,
                authorName = rawJsonObject[authorJsonKey] as? String?,
                imageUriString = rawJsonObject[imageUriStringJsonKey] as? String?
            )
        }


        operator fun invoke(jsonReader: JsonReader): ProductJson? {
            var title: String? = null
            var authorName: String? = null
            var imageUriString: String? = null

            jsonReader.forEachNameInObject { name ->
                when (name) {
                    titleJsonKey -> title = jsonReader.nextString()
                    authorJsonKey -> authorName = jsonReader.nextString()
                    imageUriStringJsonKey -> imageUriString = jsonReader.nextString()
                    else -> Log.w("Unexpected Key", "Unexpected key while deserializing ProductJson: $name")
                }
            }

            return ProductJson(
                title = title ?: return null,
                authorName = authorName,
                imageUriString = imageUriString
            )
        }
    }
}