package com.example.testjetpack.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.testjetpack.R
import com.example.testjetpack.ui.base.TestBase
import com.example.testjetpack.ui.custom.hasViewWithTextAtPosition
import com.example.testjetpack.ui.custom.isEmpty
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotificationsFragmentTest : TestBase() {

    // todo: implement with androidx.fragment:fragment-testing
    @get:Rule
    var activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    // todo: remove when implemented with androidx.fragment:fragment-testing
    override fun before() {
        super.before()

        // open drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())

        // Start the screen of fragment.
        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_notifications))

        Thread.sleep(500) // skip fragment init
    }


    @Test
    fun checkInitializedState() {

        // Check that fragment was opened.
        onView(withId(R.id.fNotificationsRoot))
            .check(matches(isDisplayed()))

        checkToolbarPlaceholder()

        onView(withId(R.id.list))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(not(isEmpty())))
            .check(hasViewWithTextAtPosition(0, "Welcome!")) // mock data
            .check(hasViewWithTextAtPosition(0, "Snooze these notifications for: an")) // mock data
    }

}