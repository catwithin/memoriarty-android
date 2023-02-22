package com.gamesofni.memoriarty.auth

import android.app.Application
import androidx.lifecycle.*
import com.gamesofni.memoriarty.DataStoreRepository
import com.gamesofni.memoriarty.Repository
import com.gamesofni.memoriarty.database.MemoriartyDatabase
import com.gamesofni.memoriarty.overview.MemoriartyApiStatus
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception


class AuthorisationViewModel (
    application: Application,
    private val dataStoreRepository: DataStoreRepository,
) : AndroidViewModel(application) {

    private val repository = Repository(MemoriartyDatabase.getInstance(application))

    private val _user = repository.user
    val user
        get() = _user


    private var _username = MutableLiveData("")
    val username = _username
    fun setUsername(it: String) { _username.value = it }

    private var _password = MutableLiveData("")
    var password = _password
    fun setPassword(it: String) { _password.value = it }

    // TODO: handle loading
    private val _status = MutableLiveData<MemoriartyApiStatus>()
    val status: LiveData<MemoriartyApiStatus> = _status
    // TODO: handle errors
    private val _networkError = MutableLiveData<Boolean>()
    val networkError: LiveData<Boolean> = _networkError


    init {
        Timber.i("AuthorisationViewModel initialized")
    }

    fun submitLogin(onLoginSuccessNavigate: () -> Unit) {
        _networkError.value = false
        viewModelScope.launch {
            _status.value = MemoriartyApiStatus.LOADING
            try {
                Timber.d("submitting login form with $username $password")
                val sessionToken = repository.loginUser(username.value?:"", password.value?:"")
                    .split(';')[0]
                Timber.d("got sessionToken from API: $sessionToken")
                if (sessionToken.isNotEmpty()) {
                    dataStoreRepository.storeSessionCookie(sessionToken)
                    onLoginSuccessNavigate()
                }
                _status.value = MemoriartyApiStatus.DONE
            } catch (e : Exception) {
                _networkError.value = true
                Timber.e(e)
                _status.value = MemoriartyApiStatus.DONE
            }
        }
    }

    fun submitLogout(onLogoutSuccessNavigate: () -> Unit) {
        viewModelScope.launch {
            dataStoreRepository.removeSession()
            onLogoutSuccessNavigate()
        }
    }
}

class AuthorisationViewModelFactory (
    private val application: Application,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthorisationViewModel::class.java)) {
            return AuthorisationViewModel(application, dataStoreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
