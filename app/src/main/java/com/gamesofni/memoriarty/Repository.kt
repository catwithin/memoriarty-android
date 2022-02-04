package com.gamesofni.memoriarty

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.gamesofni.memoriarty.database.MemoriartyDatabase
import com.gamesofni.memoriarty.database.asDomainModel
import com.gamesofni.memoriarty.network.MemoriartyApi
import com.gamesofni.memoriarty.network.asDatabaseRepeats
import com.gamesofni.memoriarty.repeat.Repeat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class Repository(private val db: MemoriartyDatabase) {
    val todayRepeats: LiveData<List<Repeat>> = Transformations.map(
        db.repeatsDao.getTodayRepeats(atStartOfDay(), atEndOfDay()))
            { it.asDomainModel()}

    val overdueRepeats: LiveData<List<Repeat>> = Transformations.map(
        db.repeatsDao.getOverdueRepeats(atStartOfDay()))
    { it.asDomainModel()}


    suspend fun refreshTodayRepeats() {
        Timber.d("refresh repeats is called");
        withContext(Dispatchers.IO) {
            val today = MemoriartyApi.retrofitService.getRepeats(Config().COOKIE)
            db.repeatsDao.insertAll(today.asDatabaseRepeats())
        }
    }


    private fun atStartOfDay(): Long { return atDayBoundary(LocalTime.MIN) }
    private fun atEndOfDay(): Long { return atDayBoundary(LocalTime.MAX) }

    private fun atDayBoundary(boundary: LocalTime): Long {
        val boundaryOfDayInLocal: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Europe/Rome")).with(boundary)
        val boundaryOfDayInUTC: ZonedDateTime =   boundaryOfDayInLocal.withZoneSameInstant(ZoneId.of
            ("Etc/UTC"))
        return boundaryOfDayInUTC.toInstant().toEpochMilli()
    }
}
