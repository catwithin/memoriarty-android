package com.gamesofni.memoriarty.network

data class LoginResponseJson (
    val token: String,
    val user: UserJson,
)
