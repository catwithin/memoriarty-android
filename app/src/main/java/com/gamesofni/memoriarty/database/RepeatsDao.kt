package com.gamesofni.memoriarty.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RepeatsDao {

    @Insert
    suspend fun insert(repeat: RepeatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( videos: List<RepeatEntity>)

    @Update
    suspend fun update(repeat: RepeatEntity)

    @Query("SELECT * from repeats WHERE repeat_id = :key")
    suspend fun get(key: String): RepeatEntity?


    @Query("DELETE FROM repeats")
    suspend fun clear()

    @Query("SELECT * FROM repeats ORDER BY next_repeat DESC LIMIT 1")
    suspend fun getRepeat(): RepeatEntity?

    @Query("SELECT * FROM repeats " +
            "WHERE next_repeat >= :startOfTodayUnix AND next_repeat <= :endOfTodayUnix " +
            "ORDER BY next_repeat DESC")
    fun getTodayRepeats(startOfTodayUnix: Long, endOfTodayUnix: Long): LiveData<List<RepeatEntity>>

    @Query("SELECT * FROM repeats ")
    fun getAllRepeats(): LiveData<List<RepeatEntity>>

    @Query("SELECT * FROM repeats WHERE description = :desc LIMIT 1")
    suspend fun getRepeatByDescription(desc: String): RepeatEntity?

}
