package com.gamesofni.memoriarty.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gamesofni.memoriarty.repeat.Repeat
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant.now
import java.util.*

const val REPEATS_TABLE = "repeats"

@Entity(tableName = REPEATS_TABLE)
data class RepeatEntity (
//    @PrimaryKey(autoGenerate = true)
//    var id: Long = 0L,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "repeat_id")
    var repeatId: String = "",

    @ColumnInfo(name = "date_created")
    var repeatCreated: Date = Date(),

    var description: String = "",

    @ColumnInfo(name = "next_repeat")
    var nextRepeat: Date = Date(),

    @ColumnInfo(name = "project_id")
    var projectId: String = "",

//    @ColumnInfo(name = "repeats")
//    var repeats: List<Date> = listOf(),

)

fun List<RepeatEntity>.asDomainModel(): List<Repeat> {
    return map {
        Repeat(
            id = it.repeatId,
            dateCreated = it.repeatCreated,
            description = it.description,
            toRepeatOn = it.nextRepeat,
            project = it.projectId,
//            repeats = it.repeats,
        )
    }
}

fun repeatUpdatePayload (repeat: Repeat): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put("session", repeatAsJson(repeat))
    jsonObject.put("date", now())

    return jsonObject
}

fun repeatAsJson(repeat: Repeat): JSONObject {
    val repeatObject = JSONObject()

    repeatObject.put("_id", repeat.id)
    repeatObject.put("description", repeat.description)
    repeatObject.put("date", repeat.createdDateToServerFormat)
    repeatObject.put("project_id", repeat.project)
    repeatObject.put("repeats", JSONArray().put(repeat.nextRepeatToServerFormat))
//    val jsonArray = JSONArray()
//    repeat.nextRepeatsToServerFormat.map {jsonArray.put(it)}
//    repeatObject.put("repeats", jsonArray)
    repeatObject.put("next_repeat", repeat.nextRepeatToServerFormat)

    return repeatObject
}
