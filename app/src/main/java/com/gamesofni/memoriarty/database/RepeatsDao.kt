package com.gamesofni.memoriarty.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.*

@Dao
interface RepeatsDao {

    @Insert
    suspend fun insert(repeat: RepeatEntity)

    @Update
    suspend fun update(repeat: RepeatEntity)

    @Query("SELECT * from repeats WHERE id = :key")
    suspend fun get(key: Long): RepeatEntity?


    @Query("DELETE FROM repeats")
    suspend fun clear()

    @Query("SELECT * FROM repeats ORDER BY next_repeat DESC LIMIT 1")
    suspend fun getRepeat(): RepeatEntity?

    @Query("SELECT * FROM repeats WHERE next_repeat = :today ORDER BY next_repeat DESC")
    fun getTodayRepeats(today: Date): LiveData<List<RepeatEntity>>

    @Query("SELECT * FROM repeats WHERE description = :desc LIMIT 1")
    suspend fun getRepeatByDescription(desc: String): RepeatEntity?

}


