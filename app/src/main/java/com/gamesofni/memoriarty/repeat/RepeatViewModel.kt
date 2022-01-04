package com.gamesofni.memoriarty.repeat

import android.app.Application
import androidx.lifecycle.*
import com.gamesofni.memoriarty.database.RepeatEntity
import com.gamesofni.memoriarty.database.RepeatsDao
import com.gamesofni.memoriarty.formatToHtml
import kotlinx.coroutines.launch
import timber.log.Timber



class RepeatViewModel (
    val repeatsDao: RepeatsDao,
    application: Application
) : AndroidViewModel(application) {

    private var repeat = MutableLiveData<RepeatEntity?>()
    val repeatTransformed = Transformations.map(repeat) { repeat ->
        formatToHtml(repeat, application.resources)
    }

    init {
        initializeRepeat()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("RepeatViewModel destroyed")
    }

    private fun initializeRepeat() {
        viewModelScope.launch {
            repeat.value = getRepeatFromDatabase()
        }
    }


    private suspend fun getRepeatFromDatabase(): RepeatEntity? {
        return repeatsDao.getRepeat()
    }

    fun onAddRepeat() {
        Timber.i("in onAddRepeat")
        viewModelScope.launch {
            val newRepeat = RepeatEntity()
            insert(newRepeat)
            repeat.value = getRepeatFromDatabase()
        }
    }

    private suspend fun insert(repeat: RepeatEntity) {
        repeatsDao.insert(repeat)
    }
}
