package com.gamesofni.memoriarty.network

import com.squareup.moshi.Json
import java.util.*

data class RepeatItem (
    val _id: String,
    val description: String,
    @Json(name = "project_id") val projectId: String,
//    @Json(name = "next_repeat") val nextRepeat: Date,
//    @Json(name = "date_created") val dateCreated: String

)
