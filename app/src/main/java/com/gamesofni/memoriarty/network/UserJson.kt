package com.gamesofni.memoriarty.network

import com.gamesofni.memoriarty.database.UserEntity
import com.gamesofni.memoriarty.user.User
import com.squareup.moshi.Json


data class UserJson (
    val username: String,
    val email: String,
    val unconfirmed: Boolean,
    val tz: String,
    @Json(name = "date_created") val dateCreated: String,
)

internal fun userFromJson(it: UserJson) = User(
    username = it.username,
    email = it.email,
    tz = it.tz,
    unconfirmed = it.unconfirmed,
    userCreated = stringToDate(it.dateCreated),
)

internal fun userEntityFromJson(it: UserJson) = UserEntity (
    username = it.username,
    email = it.email,
    tz = it.tz,
    unconfirmed = it.unconfirmed,
    userCreated = stringToDate(it.dateCreated),
)
