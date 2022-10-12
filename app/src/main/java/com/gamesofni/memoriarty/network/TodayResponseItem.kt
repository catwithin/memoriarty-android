package com.gamesofni.memoriarty.network

import com.gamesofni.memoriarty.database.RepeatEntity
import com.gamesofni.memoriarty.repeat.Repeat
import com.squareup.moshi.Json
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.ENGLISH)
val DATE_FORMAT_2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SX", Locale.ENGLISH)
val DATE_FORMAT_3 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSX", Locale.ENGLISH)
val DATE_FORMAT_4 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.ENGLISH)

data class TodayResponseItem (
    val sessions: List<RepeatJson>,
    // TODO: also parse projects
    val user: UserItem,
)

data class RepeatJson (
    val _id: String,
    @Json(name = "date") val dateCreated: String,
    val description: String,
    val next_repeat: String?,
    val project_id: String,

    val repeats: List<String>,
)


fun TodayResponseItem.asDatabaseRepeats(): List<RepeatEntity> {
    return sessions.map {
        RepeatEntity(
            repeatId = it._id,
            repeatCreated = stringToDate(it.dateCreated),
            description = it.description,
            nextRepeat = computeNextRepeat(it),
            projectId = it.project_id,
        )
    }
}

// deals with fully repeated items (next_repeat = null)
fun computeNextRepeat(repeat: RepeatJson): Date {
    // TODO: optimize - maybe additional var on server?
    if (repeat.next_repeat != null) return stringToDate(repeat.next_repeat)
    val c = Calendar.getInstance()
    c.setTime(stringToDate(repeat.dateCreated))
    val today = Calendar.getInstance()
    while (c.before(today)) {
        c.add(Calendar.YEAR, 1)
    }

    return c.time
}

fun TodayResponseItem.asDomainModel(): List<Repeat> {
    return sessions.map {
        Repeat(
            id = it._id,
            dateCreated = stringToDate(it.dateCreated),
            description = it.description,
            toRepeatOn = computeNextRepeat(it),
            project = it.project_id,
        )
    }
}

fun stringToDate(s: String): Date {
    var date = Date()
    // TODO: refactor, really lazy right now
    try {
        date = DATE_FORMAT.parse(s)
    } catch (e: ParseException) {
        try {
            date = DATE_FORMAT_2.parse(s)
        } catch (e: ParseException) {
            try {
                date = DATE_FORMAT_3.parse(s)
            } catch (e: ParseException) {
                try {
                    date = DATE_FORMAT_4.parse(s)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
        }
    }
    return date
//    string.replaceAll("Z$", "+0000"))
}

//"2021-05-22T20:55:57.022Z"