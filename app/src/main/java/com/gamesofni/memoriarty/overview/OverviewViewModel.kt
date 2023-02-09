package com.gamesofni.memoriarty.overview

import android.app.Application
import androidx.lifecycle.*
import com.gamesofni.memoriarty.DataStoreRepository
import com.gamesofni.memoriarty.Repository
import com.gamesofni.memoriarty.database.MemoriartyDatabase
import com.gamesofni.memoriarty.repeat.Repeat
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception


enum class MemoriartyApiStatus { LOADING, ERROR, DONE }


class OverviewViewModel (
    application: Application,
    private val dataStoreRepository: DataStoreRepository,
) : AndroidViewModel(application) {

    private val repository = Repository(MemoriartyDatabase.getInstance(application))

    val userPreferences = dataStoreRepository.readCookieFromDataStore

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<MemoriartyApiStatus>()
    // The external immutable LiveData for the request status
    val status: LiveData<MemoriartyApiStatus> = _status

    private val _user = repository.user
    val user
        get() = _user

    private val _todayRepeats = repository.todayRepeats
    val todayRepeats
        get() = _todayRepeats
    private val _overdueRepeats = repository.overdueRepeats
    val overdueRepeats
        get() = _overdueRepeats

    private val _networkError = MutableLiveData<Boolean>()
    val networkError: LiveData<Boolean> = _networkError

    private val _navigateToRepeatDetail = MutableLiveData<String?>()
    val navigateToRepeatDetail
        get() = _navigateToRepeatDetail

    init {
        Timber.i("OverviewViewModel initialized")
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() {
        // TODO: how to make it auto-refresh when connection is re-established??
        _networkError.value = false
        viewModelScope.launch {
            _status.value = MemoriartyApiStatus.LOADING
            try {
                Timber.tag("OverViewModel").d("before refresh")
                repository.refreshTodayRepeats(userPreferences.firstOrNull())
                _status.value = MemoriartyApiStatus.DONE

            } catch (e : Exception) {
                _networkError.value = true
                // TODO: think of how to handle errors: when use data from db, how show the
                //  network error status etc.
//                _status.value = if (todayRepeats.value.isNullOrEmpty())
//                    MemoriartyApiStatus.ERROR else
                Timber.e(e)
                _status.value = MemoriartyApiStatus.DONE
            }
        }
    }

    fun onRepeatClicked(description: String) {
        _navigateToRepeatDetail.value = description
    }

    fun onRepeatDetailNavigated() {
        _navigateToRepeatDetail.value = null
    }

    fun markAsDone(repeat: Repeat) {
        // TODO: refactor out network connection handling
        _networkError.value = false
        viewModelScope.launch {
            _status.value = MemoriartyApiStatus.LOADING
            try {
                repository.updateRepeat(repeat, userPreferences.firstOrNull())
                _status.value = MemoriartyApiStatus.DONE
            } catch (e : Exception) {
                _networkError.value = true
                Timber.e(e)
                _status.value = MemoriartyApiStatus.DONE
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("OverviewViewModel destroyed")
    }
}
