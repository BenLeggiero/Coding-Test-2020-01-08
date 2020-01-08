package me.benleggiero.codingtest2020_01_08.conveniences

import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

fun <Result> URL.withOpenConnection(processor: (connection: URLConnection) -> Result): Result {
    val connection = openConnection()
    connection.connect()
    return processor(connection)
}


fun <Result> InputStream.withBuffer(processor: (bufferedStream: BufferedInputStream) -> Result): Result {
    val bufferedStream = BufferedInputStream(this)
    val result = processor(bufferedStream)
    bufferedStream.close()
    this.close()

    return result
}


fun <Result> URL.withOpenBufferedInputStream(processor: (bufferedStream: BufferedInputStream) -> Result): Result =
    withOpenConnection { connection ->
        connection.getInputStream().withBuffer(processor)
    }


