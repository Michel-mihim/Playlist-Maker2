package com.example.playlist_maker2.utils.converters

import java.text.SimpleDateFormat
import java.util.Date

fun isoDateToYearConvert(isoDate: String): String {
    var year: String
    val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val targetDateFormat = SimpleDateFormat("yyyy")
    try {
        val date: Date = isoDateFormat.parse(isoDate)
        year = targetDateFormat.format(date)
    } catch (e: Exception) {
        year = ""
    }
    return year
}