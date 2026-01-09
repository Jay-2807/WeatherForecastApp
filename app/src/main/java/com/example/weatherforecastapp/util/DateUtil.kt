package com.example.weatherforecastapp.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtil {

    fun formatWeatherDate(dateString: String): String {

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val date = sdf.parse(dateString) ?: return dateString

        val today = Calendar.getInstance()
        val target = Calendar.getInstance()
        target.time = date

        return when {
            isSameDay(today, target) -> "Today"

            isSameDay(today.apply { add(Calendar.DAY_OF_YEAR, 1) }, target) ->
                "Tomorrow"

            else -> {
                val day = target.get(Calendar.DAY_OF_MONTH)
                val suffix = getDaySuffix(day)

                val displayFormat =
                    SimpleDateFormat("d'$suffix' MMM", Locale.ENGLISH)

                displayFormat.format(target.time)
            }
        }
    }

    fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun getDaySuffix(day: Int): String {
        return when {
            day in 11..13 -> "th"
            day % 10 == 1 -> "st"
            day % 10 == 2 -> "nd"
            day % 10 == 3 -> "rd"
            else -> "th"
        }
    }
}