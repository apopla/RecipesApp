package com.example.paulina.assignmentapplication

import android.os.SystemClock
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.clearText
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith
import com.example.paulina.assignmentapplication.recipes.view.MainActivity
import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test


/**
 * Created by Paulina on 2018-03-18.
 */

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    @Rule @JvmField
    var activityActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testSearchVisibilityTypeAndShowRecycler() {
        SystemClock.sleep(2000)
        onView(withId(R.id.recipes_list_search_edit_text)).check(matches(isDisplayed()))
        onView(withId(R.id.recipes_list_search_edit_text)).perform(clearText(),typeText("s"))
        onView(withId(R.id.recipes_list_recycler)).check(matches(isDisplayed()))
    }
}