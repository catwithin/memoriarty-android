package com.gamesofni.memoriarty.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gamesofni.memoriarty.repeat.Repeat
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

    var description: String = "Test",

    @ColumnInfo(name = "next_repeat")
    var nextRepeat: Date = Date(),

    @ColumnInfo(name = "project_id")
    var projectId: String = "",


)

fun List<RepeatEntity>.asDomainModel(): List<Repeat> {
    return map {
        Repeat(
            id = it.repeatId,
            dateCreated = it.repeatCreated,
            description = it.description,
            toRepeatOn = it.nextRepeat,
            project = it.projectId,
        )
    }
}
