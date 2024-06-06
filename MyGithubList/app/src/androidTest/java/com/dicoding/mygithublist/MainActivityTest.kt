package com.dicoding.mygithublist

import android.widget.EditText
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.dicoding.mygithublist.ui.MainActivity
import com.dicoding.mygithublist.ui.adapter.UserAdapter

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Before
    fun setup(){
        val scenario = ActivityScenario.launch(MainActivity::class.java)
    }
    @Test
    fun assertDetailUser() {
        onView(withId(R.id.homeRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<UserAdapter.MyViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.detailProfile)).check(matches(isDisplayed()))
        onView(withId(R.id.tvDetailName)).check(matches(isDisplayed()))
        onView(withId(R.id.tvUsername)).check(matches(isDisplayed()))
    }

    @Test
    fun assertSearchUser() {
        onView(withId(R.id.searchBar)).perform(click())
        onView(ViewMatchers.isAssignableFrom(EditText::class.java))
            .perform(ViewActions.typeText("test"), ViewActions.pressImeActionButton())
        onView(withId(R.id.homeRecyclerView)).check(matches(isDisplayed()))
    }


    @Test
    fun assertSettings() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText("Settings")).perform(click())
        onView(withId(R.id.switch_theme)).check(matches(isDisplayed()))
    }
}