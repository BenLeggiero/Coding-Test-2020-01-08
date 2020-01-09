package me.benleggiero.codingtest2020_01_08.conveniences

import android.content.res.Resources
import android.util.JsonReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader



// MARK: - General input stream conveniences

fun <Result> withInputStream(resourceId: Int, processor: (InputStream) -> Result): Result {
    val inputStream = Resources.getSystem().openRawResource(resourceId)
    val result = processor(inputStream)
    inputStream.close()
    return result
}


fun <Result> readStream(inputStream: InputStream, processor: (InputStreamReader) -> Result): Result {
    val inputStreamReader = InputStreamReader(inputStream)
    val result = processor(inputStreamReader)
    inputStreamReader.close()
    return result
}


fun <Result> readStream(resourceId: Int, processor: (InputStreamReader) -> Result): Result =
    withInputStream(resourceId = resourceId) { inputStream ->
        readStream(inputStream, processor)
    }


fun <Result> readJson(reader: Reader, processor: (JsonReader) -> Result): Result {
    val jsonReader = JsonReader(reader)
    val result = processor(jsonReader)
    jsonReader.close()
    return result
}


fun <Result> readJson(resourceId: Int, processor: (JsonReader) -> Result): Result =
    readStream(resourceId = resourceId) { inputStreamReader ->
        readJson(inputStreamReader, processor)
    }


fun <Result> readJson(inputStream: InputStream, processor: (JsonReader) -> Result): Result =
    readStream(inputStream) { inputStreamReader ->
        readJson(inputStreamReader, processor)
    }



// MARK: - JsonReader

fun <Result> JsonReader.readArray(processor: (JsonReader) -> Result): Result {
    beginArray()
    val result = processor(this)
    endArray()
    return result
}


fun <Result> JsonReader.readObject(processor: (JsonReader) -> Result): Result {
    beginObject()
    val result = processor(this)
    endObject()
    return result
}


fun <Result> JsonReader.mapNextArray(processor: (JsonReader) -> Result): List<Result> {
    val result = mutableListOf<Result>()

    readArray {
        while (hasNext()) {
            result.add(processor(this))
        }
    }

    return result
}


fun <Result> JsonReader.compactMapNextArray(processor: (JsonReader) -> Result?): List<Result> {
    val result = mutableListOf<Result>()

    readArray {
        while (hasNext()) {
            processor(this)?.let {
                result.add(it)
            }
        }
    }

    return result
}


fun JsonReader.forEachNameInObject(processor:  (name: String) -> Unit) {
    while (hasNext()) {
        processor(nextName())
    }
}