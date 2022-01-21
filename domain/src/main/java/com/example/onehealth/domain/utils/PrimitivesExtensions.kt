package com.example.onehealth.domain.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Float.format(decimalPlaces: Int): String {
    return this.toDouble().format(decimalPlaces)
}

fun Double.format(decimalPlaces: Int): String {
    return DecimalFormat("#.${"#".repeat(decimalPlaces)}").format(this)
}

fun Long.formatTimeStamp(format: String, timeZone: TimeZone? = null): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    timeZone?.let {
        formatter.timeZone = it
    }
    return formatter.format(Date(this))
}