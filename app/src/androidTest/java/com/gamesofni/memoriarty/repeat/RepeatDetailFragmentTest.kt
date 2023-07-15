package com.gamesofni.memoriarty.repeat

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.gamesofni.memoriarty.R
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@MediumTest
@RunWith(AndroidJUnit4::class)
class RepeatDetailFragmentTest {

    @Test
    fun activeTaskDetails_DisplayedInUi() {
        val repeat = Repeat("id", Date(), "Test repeat description", Date(), "test project id")
//        val bundle = RepeatDetailFragmentArgs(repeat.description).toBundle()
        val bundle = bundleOf("description" to repeat.description)
        launchFragmentInContainer<RepeatDetailFragment>(bundle, R.style.Theme_Memoriarty)
        Thread.sleep(2000)
    }

}
