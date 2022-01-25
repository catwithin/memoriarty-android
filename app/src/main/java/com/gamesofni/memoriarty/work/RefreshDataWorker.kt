package com.gamesofni.memoriarty.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gamesofni.memoriarty.Repository
import com.gamesofni.memoriarty.database.MemoriartyDatabase
import retrofit2.HttpException

class RefreshDataWorker (appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "com.gamesofni.memoriarty.work.RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = MemoriartyDatabase.getInstance(applicationContext)
        val repository = Repository(database)
        try {
            repository.refreshTodayRepeats()
        } catch (e: HttpException) {
            return Result.retry()
        }

        return Result.success()
    }

}
