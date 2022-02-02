package com.gamesofni.memoriarty.repeat

import com.gamesofni.memoriarty.smartTruncate
import java.util.*


data class Repeat(
    val id: String,

    val dateCreated: Date,
    val description: String,
    val toRepeatOn: Date,
    val project: String,
    ) {

    val shortDescription: String
        get() = description.smartTruncate(50)

    val projectInitials: String
        get() = project.substring(0,2)

    val dateToText: String
        get() = toRepeatOn.toString()
}
