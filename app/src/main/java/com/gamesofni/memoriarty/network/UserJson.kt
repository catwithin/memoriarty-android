package com.gamesofni.memoriarty.network

import com.gamesofni.memoriarty.database.UserEntity
import com.gamesofni.memoriarty.user.User
import com.squareup.moshi.Json


data class UserJson (
    val username: String,
    val email: String,
    // TODO: deal with unconfirmed user
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

data class SignUpResponseJson (
    val error: ErrorMessageJson?,
    val user: UserJson?, // 200
)

data class ErrorMessageJson (
    val internal: String?, // 500 (non-existing email) TODO: fix this on server side
    val general: String?, // 400 not all fields
    val email: String?, // 409 conflict or 400 invalid email
    val password: String?, // 409 conflict or 400 invalid
    val username: String?, // 409 conflict or 400 invalid
)
