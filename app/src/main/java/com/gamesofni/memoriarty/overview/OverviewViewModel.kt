package com.gamesofni.memoriarty.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamesofni.memoriarty.Config
import com.gamesofni.memoriarty.network.MemoriartyApi
import com.gamesofni.memoriarty.network.RepeatItem
import kotlinx.coroutines.launch



enum class MemoriartyApiStatus { LOADING, ERROR, DONE }


class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<MemoriartyApiStatus>()
    private val _repeats = MutableLiveData<List<RepeatItem>>()

    // The external immutable LiveData for the request status
    val status: LiveData<MemoriartyApiStatus> = _status
    val repeats: LiveData<List<RepeatItem>> = _repeats

    init {
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
}
