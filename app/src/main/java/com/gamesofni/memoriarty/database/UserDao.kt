package com.gamesofni.memoriarty.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): LiveData<UserEntity>

// no update? if couldn't change on server - error and tz isn't changed? otherwise -
// insert=replace with the response from the server

    @Query("DELETE FROM user")
    suspend fun deleteUser()

}
