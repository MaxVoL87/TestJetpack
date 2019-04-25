package com.example.testjetpack.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.testjetpack.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.DrawerActions
import android.view.Gravity
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import com.example.testjetpack.ui.base.TestBase


@RunWith(AndroidJUnit4::class)
class MainActivityTest : TestBase() {

    @get:Rule
    var activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun checkInitializedState() {
        // checkToolbarIsDisplayed
        onView(withId(R.id.cToolbar)).check(matches(isDisplayed()))

        // checkNavigationIsClosed
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)))

        checkDefaultFragmentIsShowing()

        // progress dialog not showing
        onView(withId(R.id.pdRoot)).check(doesNotExist())
    }

    @Test
    fun checkOpenMyProfileAction() {
        checkNavDrawerAction(R.id.nav_my_profile, R.id.fMyProfileRoot, 2500)

//        val expectedNoStatisticsText =
//            InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.no_item_available)
    }

    @Test
    fun checkOpenMyTripAction() {
        checkNavDrawerAction(R.id.nav_my_trip, R.id.fMyTripRoot, 500)
    }

    @Test
    fun checkOpenGPSAction() {
        checkNavDrawerAction(R.id.nav_gps, R.id.fGPSRoot, 500)
    }

    @Test
    fun checkOpenNotificationsAction() {
        checkNavDrawerAction(R.id.nav_notifications, R.id.fNotificationsRoot, 500)
    }

    private fun checkNavDrawerAction(@IdRes actionId: Int, @IdRes fragmentRootId: Int, delay: Long){
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
            .perform(DrawerActions.open()) // Open Drawer

        // Check Drawer opened
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()))

        // Start the screen of fragment.
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(actionId))

        Thread.sleep(delay) // skip my profile init requests
        // Check that fragment was opened.
        onView(withId(fragmentRootId)).check(matches(isDisplayed()))

        // Check Nav Drawer should be closed.
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))

        //todo: check naw drawer can't be opened

        // progress dialog not showing
        onView(withId(R.id.pdRoot)).check(doesNotExist())

        // not working todo: check back button is displayed
        // onView(withId(android.R.id.home)).check(matches(isDisplayed()))

        Espresso.pressBack()

        checkDefaultFragmentIsShowing()
    }

    private fun checkDefaultFragmentIsShowing(){
        // check default Fragment is showing
        onView(withId(R.id.fGitreposearchRoot)).check(matches(isDisplayed()))
    }
}