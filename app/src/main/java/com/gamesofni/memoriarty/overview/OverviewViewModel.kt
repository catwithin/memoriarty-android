package com.gamesofni.memoriarty.overview

import android.app.Application
import androidx.lifecycle.*
import com.gamesofni.memoriarty.Config
import com.gamesofni.memoriarty.database.RepeatsDao
import com.gamesofni.memoriarty.network.MemoriartyApi
import com.gamesofni.memoriarty.network.RepeatItem
import kotlinx.coroutines.launch
import timber.log.Timber


enum class MemoriartyApiStatus { LOADING, ERROR, DONE }


class OverviewViewModel (
    repeatsDao: RepeatsDao,
    application: Application
) : AndroidViewModel(application) {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<MemoriartyApiStatus>()
    // The external immutable LiveData for the request status
    val status: LiveData<MemoriartyApiStatus> = _status

    private val _repeats = MutableLiveData<List<RepeatItem>>()
    val repeats: LiveData<List<RepeatItem>> = _repeats

    private val _navigateToRepeatDetail = MutableLiveData<String?>()
    val navigateToRepeatDetail
        get() = _navigateToRepeatDetail

    init {
        Timber.i("OverviewViewModel initialized")
        getTodayRepeats()
    }

    private fun getTodayRepeats() {
        viewModelScope.launch {
            // TODO: how to make it auto-refresh when connection is re-established??
            _status.value = MemoriartyApiStatus.LOADING
            try {
                val today = MemoriartyApi.retrofitService.getRepeats(Config().COOKIE)
                _repeats.value = today.sessions

                _status.value = MemoriartyApiStatus.DONE

            } catch (e: Exception) {
                // catching no internet exception
                _status.value = MemoriartyApiStatus.ERROR
                _repeats.value = listOf()
            }
        }
    }

    fun onRepeatClicked(description: String) {
        _navigateToRepeatDetail.value = description
    }

    fun onRepeatDetailNavigated() {
        _navigateToRepeatDetail.value = null
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("OverviewViewModel destroyed")
    }
}
