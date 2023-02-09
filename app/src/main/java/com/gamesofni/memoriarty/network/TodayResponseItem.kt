package com.gamesofni.memoriarty.network

import com.gamesofni.memoriarty.database.RepeatEntity
import com.gamesofni.memoriarty.repeat.Repeat


data class TodayResponseItem (
    val sessions: List<ChunkJson>,
    // TODO: also parse projects
    val user: UserJson,
)

fun TodayResponseItem.asDatabaseRepeats(): List<RepeatEntity> {
    return sessions.map {
        repeatEntityFromChunk(it)
    }
}

fun TodayResponseItem.asDomainModel(): List<Repeat> {
    return sessions.map { it ->
        repeatFromChunk(it)
    }
}
