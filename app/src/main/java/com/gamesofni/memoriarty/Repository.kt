package com.gamesofni.memoriarty

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.gamesofni.memoriarty.DataStoreRepository.PreferencesKeys.SESSION_KEY
import com.gamesofni.memoriarty.database.MemoriartyDatabase
import com.gamesofni.memoriarty.database.asDomainModel
import com.gamesofni.memoriarty.database.repeatUpdatePayload
import com.gamesofni.memoriarty.network.MemoriartyApi
import com.gamesofni.memoriarty.network.asDatabaseRepeats
import com.gamesofni.memoriarty.network.repeatEntityFromChunk
import com.gamesofni.memoriarty.network.userEntityFromJson
import com.gamesofni.memoriarty.repeat.Repeat
import com.gamesofni.memoriarty.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import timber.log.Timber
import java.io.IOException
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime


private const val USER_PREFERENCES_NAME = "user_preferences"

val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class Repository(private val db: MemoriartyDatabase) {

    val user: LiveData<User> = Transformations.map(db.userDao.getUser()) {it.asDomainModel()}

    val todayRepeats: LiveData<List<Repeat>> = Transformations.map(
        db.repeatsDao.getTodayRepeats(atStartOfDay(), atEndOfDay()))
            { it.asDomainModel()}

    val overdueRepeats: LiveData<List<Repeat>> = Transformations.map(
        db.repeatsDao.getOverdueRepeats(atStartOfDay()))
    { it.asDomainModel()}

    suspend fun refreshTodayRepeats(userPreferences: UserPreferences?) {
        Timber.d("refresh repeats is called");
        Timber.d("session from settings: %s", userPreferences.toString());
        withContext(Dispatchers.IO) {
            val today = MemoriartyApi.retrofitService.getRepeats(userPreferences?.session ?: "")
            // TODO: do in transaction
            db.repeatsDao.clear()
            db.repeatsDao.insertAll(today.asDatabaseRepeats())
            db.userDao.deleteUser()
            db.userDao.insert(userEntityFromJson(today.user))
        }
    }

    private fun atStartOfDay(): Long { return atDayBoundary(LocalTime.MIN) }
    private fun atEndOfDay(): Long { return atDayBoundary(LocalTime.MAX) }

    private fun atDayBoundary(boundary: LocalTime): Long {
        val boundaryOfDayInLocal: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Europe/Rome")).with(boundary)
        val boundaryOfDayInUTC: ZonedDateTime =   boundaryOfDayInLocal.withZoneSameInstant(ZoneId.of
            ("Etc/UTC"))
        return boundaryOfDayInUTC.toInstant().toEpochMilli()
    }

    private val JSON_TYPE = MediaType.parse("application/json")

    suspend fun updateRepeat(repeat: Repeat, userPreferences: UserPreferences?) {
        Timber.d("update repeat is called with $repeat");
        val jsonObjectString = repeatUpdatePayload(repeat).toString()
        Timber.d("sending update $jsonObjectString");

        val body: RequestBody = RequestBody.create(JSON_TYPE, jsonObjectString)

        withContext(Dispatchers.IO) {
            val updatedChunk = MemoriartyApi.retrofitService.putRepeat(userPreferences?.session
                ?: "", body)
            db.repeatsDao.update(repeatEntityFromChunk(updatedChunk))
        }
    }
}


data class UserPreferences(
    val session: String,
)

class DataStoreRepository(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val SESSION_KEY = stringPreferencesKey("session")
    }

    suspend fun storeSessionCookie(session: String) {
        Timber.tag("DataStoreRepository").e("saving cookie: %s", session)
        dataStore.edit { preference ->
            preference[SESSION_KEY] = session
        }
    }

    val readCookieFromDataStore : Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.tag("DataStoreRepository").d(exception.message.toString())
                Timber.tag("DataStoreRepository").e("emit emptyPreferences()")
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val session = preference[SESSION_KEY] ?: ""
            Timber.tag("DataStoreRepository").e("in reading cookie: %s", session)
            UserPreferences(session)
        }

    suspend fun clearDataStore() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun removeSession() {
        dataStore.edit { preference ->
            preference.remove(SESSION_KEY)
        }
    }
}
