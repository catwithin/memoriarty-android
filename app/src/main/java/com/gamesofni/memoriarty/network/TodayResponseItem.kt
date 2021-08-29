package com.gamesofni.memoriarty.network


data class TodayResponseItem (
    // fields' names and types correspond to the key names and value types in JSON
    val sessions: List<RepeatItem>,
    val user: UserItem,
    // for JSON key names that can't be directly translated:
//    @Json(name = "img_src") val imgSrcUrl: String

)
