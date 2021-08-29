package com.gamesofni.memoriarty.network

import com.squareup.moshi.Json

data class RepeatItem (
    val _id: String,
    val description: String,
    @Json(name = "project_id") val projectId: String
//    @Json(name = "date_created") val dateCreated: String

)
