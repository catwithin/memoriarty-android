package com.gamesofni.memoriarty.user

import java.time.Instant
import java.util.*

data class User(
    val username: String,
    val email: String,
    val tz: String,
    val unconfirmed: Boolean,
    val userCreated: Date,

) {

    val dateCreatedToText: String
        get() = userCreated.toString()

    val createdDateToServerFormat: Instant?
        get() = userCreated.toInstant()

}
