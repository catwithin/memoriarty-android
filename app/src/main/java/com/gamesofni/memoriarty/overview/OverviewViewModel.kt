package com.gamesofni.memoriarty.overview

import android.app.Application
import androidx.lifecycle.*
import com.gamesofni.memoriarty.Repository
import com.gamesofni.memoriarty.database.MemoriartyDatabase
import com.gamesofni.memoriarty.database.RepeatsDao
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.lang.Exception


enum class MemoriartyApiStatus { LOADING, ERROR, DONE }


class OverviewViewModel (
    repeatsDao: RepeatsDao,
    application: Application
) : AndroidViewModel(application) {

    private val repository = Repository(MemoriartyDatabase.getInstance(application))

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<MemoriartyApiStatus>()
    // The external immutable LiveData for the request status
    val status: LiveData<MemoriartyApiStatus> = _status

    val repeats = repository.repeats


    private val _networkError = MutableLiveData<Boolean>()
    val networkError: LiveData<Boolean> = _networkError

    private val _navigateToRepeatDetail = MutableLiveData<String?>()
    val navigateToRepeatDetail
        get() = _navigateToRepeatDetail

    init {
        Timber.i("OverviewViewModel initialized")
        refreshDataFromRepository()
    }

//    private fun getTodayRepeats() {
//        viewModelScope.launch {
//            // TODO: how to make it auto-refresh when connection is re-established??
//            _status.value = MemoriartyApiStatus.LOADING
//            try {
//                val today = MemoriartyApi.retrofitService.getRepeats(Config().COOKIE)
//                _repeats.value = today.sessions
//
//                _status.value = MemoriartyApiStatus.DONE
//
//            } catch (e: Exception) {
//                // catching no internet exception
//                _status.value = MemoriartyApiStatus.ERROR
//                _repeats.value = listOf()
//            }
//        }
//    }

    private fun refreshDataFromRepository() {
        _networkError.value = false
        viewModelScope.launch {
            _status.value = MemoriartyApiStatus.LOADING
            try {
                repository.refreshTodayRepeats()
                _status.value = MemoriartyApiStatus.DONE

            } catch (e : Exception) {
                _networkError.value = true
                _status.value = if (repeats.value.isNullOrEmpty())
                    MemoriartyApiStatus.ERROR else
                    MemoriartyApiStatus.DONE
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
