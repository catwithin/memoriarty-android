package com.gamesofni.memoriarty.repeat

import android.app.Application
import androidx.lifecycle.*
import com.gamesofni.memoriarty.database.RepeatEntity
import com.gamesofni.memoriarty.database.RepeatsDao
import com.gamesofni.memoriarty.formatToHtml
import kotlinx.coroutines.launch
import timber.log.Timber



class RepeatViewModel (
    private var repeatId: String?,
    val repeatsDao: RepeatsDao,
    application: Application,
) : AndroidViewModel(application) {

    var repeat = MutableLiveData<RepeatEntity?>()
    var idString = Transformations.map(repeat) { r -> r?.repeatId }

    val repeatTransformed = Transformations.map(repeat) { repeat ->
        formatToHtml(repeat, application.resources)
    }

    private val _navigateToEditRepeat = MutableLiveData<RepeatEntity?>()
    val navigateToEditRepeat: LiveData<RepeatEntity?>
        get() = _navigateToEditRepeat

    private val _navigateToDetailRepeat = MutableLiveData<RepeatEntity?>()
    val navigateToDetailRepeat: LiveData<RepeatEntity?>
        get() = _navigateToDetailRepeat

    private val _navigateToOverview = MutableLiveData<RepeatEntity?>()
    val navigateToOverview: LiveData<RepeatEntity?>
        get() = _navigateToOverview


    init {
        initializeRepeat()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("RepeatViewModel destroyed")
    }

    private fun initializeRepeat() {
        viewModelScope.launch {
            repeat.value = getRepeatFromDatabase(repeatId ?: "Test")
        }
    }


    private suspend fun getRepeatFromDatabase(id: String): RepeatEntity? {
        return repeatsDao.getRepeatByDescription(id)
    }

    fun onAddRepeat() {
        Timber.i("in onAddRepeat")
        viewModelScope.launch {
            val newRepeat = RepeatEntity()
            insert(newRepeat)
            repeat.value = getRepeatFromDatabase("Test")
        }
    }

    private suspend fun insert(repeat: RepeatEntity) {
        repeatsDao.insert(repeat)
    }

    fun doneNavigatingToEdit() {
        _navigateToEditRepeat.value = null
    }

    fun doneNavigatingToDetail() {
        _navigateToDetailRepeat.value = null
    }

    fun onEditRepeat() {
        _navigateToEditRepeat.value = repeat.value
    }

    fun onDoneEdit() {
        viewModelScope.launch {
        // In Kotlin, the return@label syntax is used for specifying which function among
        // several nested ones this statement returns from.
        // In this case, we are specifying to return from launch(),
        // not the lambda.
            val oldRepeat = repeatsDao.get(repeat.value!!.repeatId) ?: return@launch
            oldRepeat.description = repeat.value!!.description
            repeatsDao.update(oldRepeat)

            _navigateToDetailRepeat.value = repeat.value
        }
    }

    fun onCheckedClicked() {
        viewModelScope.launch {
            // TODO: implement db repeat done update
//            val oldRepeat = repeatsDao.get(repeat.value!!.repeatId) ?: return@launch
//            oldRepeat.description = repeat.value!!.description
//            repeatsDao.update(oldRepeat)

            _navigateToOverview.value = repeat.value
        }
    }
    fun doneNavigatingToOverview() {
        _navigateToOverview.value = null
    }

}

