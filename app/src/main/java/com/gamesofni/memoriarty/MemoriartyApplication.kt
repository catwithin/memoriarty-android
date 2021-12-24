package com.gamesofni.memoriarty

import android.app.Application
import timber.log.Timber

class MemoriartyApplication  : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}