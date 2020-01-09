package me.benleggiero.codingtest2020_01_08.conveniences


import kotlin.text.RegexOption.*

/**
 * If this string is a HTTP URL, this will return a HTTPS version of it
 */
val String.enforcingHttps: String
    get() = replaceFirst(regex = Regex("^http\\b", IGNORE_CASE), replacement = "https")
