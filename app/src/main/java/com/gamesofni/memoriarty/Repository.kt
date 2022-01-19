package com.gamesofni.memoriarty

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.gamesofni.memoriarty.database.MemoriartyDatabase
import com.gamesofni.memoriarty.database.RepeatEntity
import com.gamesofni.memoriarty.database.asDomainModel
import com.gamesofni.memoriarty.network.MemoriartyApi
import com.gamesofni.memoriarty.network.asDatabaseRepeats
import com.gamesofni.memoriarty.repeat.Repeat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

class Repository(private val db: MemoriartyDatabase) {
    val repeats: LiveData<List<Repeat>> = Transformations.map(
        db.repeatsDao.getAllRepeats()) { it.asDomainModel() }
//        db.repeatsDao.getTodayRepeats(Date())) { it.asDomainModel() }

    suspend fun refreshTodayRepeats() {
        Timber.d("refresh repeats is called");
        withContext(Dispatchers.IO) {
            val today = MemoriartyApi.retrofitService.getRepeats(Config().COOKIE)
            db.repeatsDao.insertAll(today.asDatabaseRepeats())
        }
    }

}