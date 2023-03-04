package com.gamesofni.memoriarty.auth

import android.app.Application
import androidx.lifecycle.*
import com.gamesofni.memoriarty.DataStoreRepository
import com.gamesofni.memoriarty.Repository
import com.gamesofni.memoriarty.database.MemoriartyDatabase
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

enum class MemoriartyLoginStatus { LOGGED_OUT, LOADING, WRONG_LOGIN, NETWORK_ERROR, LOGGED_IN }

class AuthorisationViewModel (
    application: Application,
    private val dataStoreRepository: DataStoreRepository,
) : AndroidViewModel(application) {

    private val repository = Repository(MemoriartyDatabase.getInstance(application))

    private val _user = repository.user
    val user
        get() = _user

    private var _email = MutableLiveData("")
    val email = _email
    fun setEmail(it: String) { _email.value = it }

    private var _username = MutableLiveData("")
    val username = _username
    fun setUsername(it: String) { _username.value = it }

    private var _password = MutableLiveData("")
    var password = _password
    fun setPassword(it: String) { _password.value = it }

    private val _status = MutableLiveData<MemoriartyLoginStatus>()
    val status: LiveData<MemoriartyLoginStatus> = _status

    init {
        Timber.i("AuthorisationViewModel initialized")
        clearStatus()
    }

    fun submitLogin() {
        _status.value = MemoriartyLoginStatus.LOADING
        viewModelScope.launch {
            try {
                Timber.d("submitting login form with $username $password")
                val (token, responseStatus) = repository
                    .loginUser(username.value?:"", password.value?:"")
                val sessionToken = token.split(';')[0]
                Timber.d("got sessionToken from API: $sessionToken; status response: $responseStatus")
                if (sessionToken.isNotEmpty()) {
                    dataStoreRepository.storeSessionCookie(sessionToken)
                }
                _status.value = responseStatus
            } catch (e : Exception) {
                Timber.e(e)
                _status.value = MemoriartyLoginStatus.NETWORK_ERROR
            }
        }
    }


    fun submitSignup() {
        _status.value = MemoriartyLoginStatus.LOADING
        viewModelScope.launch {
            try {
                Timber.d("submitting login form with $username $password")
                val (token, responseStatus) = repository
                    .loginUser(username.value?:"", password.value?:"")
                val sessionToken = token.split(';')[0]
                Timber.d("got sessionToken from API: $sessionToken; status response: $responseStatus")
                if (sessionToken.isNotEmpty()) {
                    dataStoreRepository.storeSessionCookie(sessionToken)
                }
                _status.value = responseStatus
            } catch (e : Exception) {
                Timber.e(e)
                _status.value = MemoriartyLoginStatus.NETWORK_ERROR
            }
        }
    }

    // TODO: check wheter backend has invalidate session/logout callbk
    fun submitLogout(onLogoutSuccessNavigate: () -> Unit) {
        viewModelScope.launch {
            dataStoreRepository.removeSession()
            clearStatus()
            onLogoutSuccessNavigate()
        }
    }

    fun clearStatus() {
        _status.value = MemoriartyLoginStatus.LOGGED_OUT
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
