package com.gamesofni.memoriarty

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.gamesofni.memoriarty.database.RepeatEntity
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun formatToHtml(repeat: RepeatEntity?, resources: Resources): Spanned {
    var str = ""
    repeat?.let {
        val sb = StringBuilder()
        sb.apply {
            append(resources.getString(R.string.title))
            append("<br>")
            append("Right now is: \t${convertLongToDateString(System.currentTimeMillis())}<br>")
            append(resources.getString(R.string.fancy_formatting))
            append("${repeat.description}<br>")
            append("id: ")
            append("${repeat.id}<br><br>")
        }
        str = sb.toString()
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(str, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

/**
 * Take the Long milliseconds returned by the system and stored in Room,
 * and convert it to a nicely formatted string for display.
 *
 * EEEE - Display the long letter version of the weekday
 * MMM - Display the letter abbreviation of the nmotny
 * dd-yyyy - day in month and full year numerically
 * HH:mm - Hours and minutes in 24hr format
 */
@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
        .format(systemTime).toString()
}



private val ONE_MINUTE_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
private val ONE_HOUR_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

fun convertDurationToFormatted(startTimeMilli: Long, endTimeMilli: Long, res: Resources): String {
    val durationMilli = endTimeMilli - startTimeMilli
    val weekdayString = SimpleDateFormat("EEEE", Locale.getDefault()).format(startTimeMilli)
    return when {
        durationMilli < ONE_MINUTE_MILLIS -> {
            val seconds = TimeUnit.SECONDS.convert(durationMilli, TimeUnit.MILLISECONDS)
            res.getString(R.string.seconds_length, seconds, weekdayString)
        }
        durationMilli < ONE_HOUR_MILLIS -> {
            val minutes = TimeUnit.MINUTES.convert(durationMilli, TimeUnit.MILLISECONDS)
            res.getString(R.string.minutes_length, minutes, weekdayString)
        }
        else -> {
            val hours = TimeUnit.HOURS.convert(durationMilli, TimeUnit.MILLISECONDS)
            res.getString(R.string.hours_length, hours, weekdayString)
        }
    }
}
