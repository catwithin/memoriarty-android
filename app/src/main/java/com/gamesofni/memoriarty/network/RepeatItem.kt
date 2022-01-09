package com.gamesofni.memoriarty.network

import com.squareup.moshi.Json
import java.util.*

data class RepeatItem (
    val _id: String,
    val description: String,
    @Json(name = "project_id") val projectId: String,
//    @Json(name = "next_repeat") val nextRepeat: Date,
    // Date date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(string.replaceAll("Z$", "+0000"));
//    @Json(name = "date_created") val dateCreated: String

)
