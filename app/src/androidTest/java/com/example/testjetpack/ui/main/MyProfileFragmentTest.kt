package com.example.testjetpack.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions.navigateTo
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.testjetpack.R
import com.example.testjetpack.ui.base.TestBase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyProfileFragmentTest : TestBase() {

    // todo: implement with androidx.fragment:fragment-testing
    @get:Rule
    var activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    // todo: remove when implemented with androidx.fragment:fragment-testing
    override fun setUp() {
        super.setUp()

        // open drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())

        // Start the screen of fragment.
        onView(ViewMatchers.withId(R.id.nav_view)).perform(navigateTo(R.id.nav_my_profile))

        Thread.sleep(2500)
    }

    @Test
    fun checkInitializedState(){

        // Check that fragment was opened.
        onView(withId(R.id.fMyProfileRoot)).check(matches(ViewMatchers.isDisplayed()))
    }

}