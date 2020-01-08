package me.benleggiero.codingtest2020_01_08.conveniences

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.BufferedInputStream
import java.net.URL


@Throws
fun Bitmap(jpegImageUrl: URL) = jpegImageUrl.withOpenBufferedInputStream { bufferedInputStream ->
    BitmapFactory.decodeStream(bufferedInputStream)
}