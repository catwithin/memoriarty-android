package com.gamesofni.memoriarty

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.gamesofni.memoriarty.DataStoreRepository.PreferencesKeys.SESSION_KEY
import com.gamesofni.memoriarty.auth.MemoriartyLoginStatus
import com.gamesofni.memoriarty.database.MemoriartyDatabase
import com.gamesofni.memoriarty.database.asDomainModel
import com.gamesofni.memoriarty.database.repeatUpdatePayload
import com.gamesofni.memoriarty.network.*
import com.gamesofni.memoriarty.network.repeatEntityFromChunk
import com.gamesofni.memoriarty.network.userEntityFromJson
import com.gamesofni.memoriarty.overview.MemoriartyApiStatus
import com.gamesofni.memoriarty.repeat.Repeat
import com.gamesofni.memoriarty.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import org.json.JSONObject
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

    val user: LiveData<User> = db.userDao.getUser().map() {it.asDomainModel()}

    val todayRepeats: LiveData<List<Repeat>> = db.repeatsDao.getTodayRepeats(atStartOfDay(), atEndOfDay())
        .map() { it.asDomainModel()}

    val overdueRepeats: LiveData<List<Repeat>> =
        db.repeatsDao.getOverdueRepeats(atStartOfDay())
            .map() { it.asDomainModel()}

    suspend fun refreshTodayRepeats(userPreferences: UserPreferences): MemoriartyApiStatus {
        Timber.d("refresh repeats is called");
        Timber.d("session from settings: %s", userPreferences.session);
        var result: MemoriartyApiStatus
        withContext(Dispatchers.IO) {
            val response = MemoriartyApi.retrofitService.getRepeats(userPreferences.session)
            if (response.isSuccessful) {
                val today = response.body()!!
                // TODO: do in transaction
                db.repeatsDao.clear()
                db.repeatsDao.insertAll(today.asDatabaseRepeats())
                db.userDao.deleteUser()
                db.userDao.insert(userEntityFromJson(today.user))
                result = MemoriartyApiStatus.DONE
            } else if (response.code() == 401) {
                result = MemoriartyApiStatus.UNAUTHORISED
            } else {
                result = MemoriartyApiStatus.NETWORK_ERROR
            }
        }
        return result
    }

    private fun atStartOfDay(): Long { return atDayBoundary(LocalTime.MIN) }
    private fun atEndOfDay(): Long { return atDayBoundary(LocalTime.MAX) }

    private fun atDayBoundary(boundary: LocalTime): Long {
        val boundaryOfDayInLocal: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Europe/Rome")).with(boundary)
        val boundaryOfDayInUTC: ZonedDateTime =   boundaryOfDayInLocal.withZoneSameInstant(ZoneId.of
            ("Etc/UTC"))
        return boundaryOfDayInUTC.toInstant().toEpochMilli()
    }

    private val JSON_TYPE = "application/json".toMediaType()

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

    suspend fun loginUser(username: String, password: String): Pair<String, MemoriartyLoginStatus> {
        Timber.d("login user: $username $password")
        val jsonObjectString = userLoginPayload(username, password).toString()
        val body: RequestBody = RequestBody.create(JSON_TYPE, jsonObjectString)
        var token = ""
        var status: MemoriartyLoginStatus
        withContext(Dispatchers.IO) {
            val response = MemoriartyApi.retrofitService.loginUser(body)
            Timber.d("loginResponse: $response")
            if (response.isSuccessful()) {
                db.userDao.deleteUser()
                // TODO: API 200 always sends user, but still feels bad to use !!
                db.userDao.insert(userEntityFromJson(response.body()!!.user))
                // TODO: maybe do in intercepter
                token = response.headers().get("Set-Cookie") ?: ""
                status = MemoriartyLoginStatus.LOGGED_IN
            } else if (response.code() == 401) {
                status = MemoriartyLoginStatus.WRONG_LOGIN
            } else {
                status = MemoriartyLoginStatus.NETWORK_ERROR
            }
        }
        return Pair(token, status)
    }
}

fun userLoginPayload (username: String, password: String): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put("username", username)
    jsonObject.put("password", password)
    return jsonObject
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
            Timber.tag("DataStoreRepository").e("removing cookie: %s", preference[SESSION_KEY])
            preference.remove(SESSION_KEY)
        }
    }
}
