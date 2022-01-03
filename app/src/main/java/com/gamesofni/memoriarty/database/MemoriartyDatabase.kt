package com.gamesofni.memoriarty.database

import android.content.Context
import androidx.room.*
import java.util.*

const val DATABASE_NAME = "memoriarty_database"

@Database(entities = [RepeatEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class  MemoriartyDatabase : RoomDatabase() {

    abstract val repeatsDao: RepeatsDao

    companion object {

        @Volatile
        private var INSTANCE: MemoriartyDatabase? = null


        fun getInstance(context: Context): MemoriartyDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MemoriartyDatabase::class.java,
                        DATABASE_NAME)
                            // always recreating db
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }

        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}
