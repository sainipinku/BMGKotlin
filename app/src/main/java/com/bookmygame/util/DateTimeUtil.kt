package com.bookmygame.util

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {

    fun format(dateTime: String): String {
        // 2021-09-26T00:00:00.000+05:30
        val dateInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(dateTime)
        return SimpleDateFormat("yyyy-MM-dd").format(dateInput)
    }

    fun millisecondToDate(milliseconds: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliseconds
        return formatter.format(calendar.time)
    }
}