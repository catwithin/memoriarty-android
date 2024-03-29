package com.gamesofni.memoriarty.repeat

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gamesofni.memoriarty.database.RepeatsDao

class RepeatViewModelFactory (
    private var repeatId: String?,
    private val repeatsDao: RepeatsDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepeatViewModel::class.java)) {
            return RepeatViewModel(repeatId, repeatsDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
