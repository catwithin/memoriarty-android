package com.gamesofni.memoriarty.repeat

import com.gamesofni.memoriarty.smartTruncate
import java.time.Instant
import java.util.*


data class Repeat(
    val id: String,

    val dateCreated: Date,
    val description: String,
    val toRepeatOn: Date,
    val project: String,
//    val repeats: List<Date>,
    ) {

    val shortDescription: String
        get() = description.smartTruncate(50)

    val projectInitials: String
        get() = project.substring(0,2)

    val dateToText: String
        get() = toRepeatOn.toString()

    val createdDateToServerFormat: Instant?
        get() = dateCreated.toInstant()

    val nextRepeatToServerFormat: Instant?
        get() = toRepeatOn.toInstant()

//    val nextRepeatsToServerFormat: List<Instant>
//        get() = repeats.map{ it.toInstant() }
}
