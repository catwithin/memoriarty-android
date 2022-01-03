package com.gamesofni.memoriarty.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

const val REPEATS_TABLE = "repeats"

@Entity(tableName = REPEATS_TABLE)
data class RepeatEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "repeat_id")
    var repeatId: String = "",

    var description: String = "",

    @ColumnInfo(name = "date_created")
    var repeatCreated: Date = Date(),

    @ColumnInfo(name = "project_id")
    var projectId: String = "",

    @ColumnInfo(name = "next_repeat")
    var nextRepeat: Date = Date(),

)
