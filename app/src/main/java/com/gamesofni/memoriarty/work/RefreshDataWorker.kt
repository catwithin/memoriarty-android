package com.gamesofni.memoriarty.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gamesofni.memoriarty.DataStoreRepository
import com.gamesofni.memoriarty.Repository
import com.gamesofni.memoriarty.dataStore
import com.gamesofni.memoriarty.database.MemoriartyDatabase
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

class RefreshDataWorker (appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "com.gamesofni.memoriarty.work.RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = MemoriartyDatabase.getInstance(applicationContext)
        val repository = Repository(database)
        val userPreferences = DataStoreRepository(applicationContext.dataStore)
            .readCookieFromDataStore
        try {
            repository.refreshTodayRepeats(userPreferences.first())
        } catch (e: HttpException) {
            return Result.retry()
        }

        return Result.success()
    }

}
