package com.gamesofni.memoriarty

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.gamesofni.memoriarty.database.MemoriartyDatabase
import com.gamesofni.memoriarty.database.RepeatEntity
import com.gamesofni.memoriarty.database.RepeatsDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class MemoriartyDatabaseTest {

    private lateinit var repeatsDao: RepeatsDao
    private lateinit var db: MemoriartyDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, MemoriartyDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        repeatsDao = db.repeatsDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetRepeat() {
        val repeat = RepeatEntity(0, "", "test", Date(), "", Date())
//        repeatsDao.insert(repeat)
//        val repeatFromDb = repeatsDao.getRepeat()
//        assertEquals(repeatFromDb?.description, "test")
    }
}

