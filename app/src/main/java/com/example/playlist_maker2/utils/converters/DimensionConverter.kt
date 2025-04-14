package com.example.playlist_maker2.utils.converters

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Date

fun dimensionsFloatToIntConvert(dim: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dim,
        context.resources.displayMetrics
    ).toInt()
}