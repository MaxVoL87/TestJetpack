package com.example.testjetpack.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.testjetpack.R
import com.example.testjetpack.ui.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.DrawerActions
import android.view.Gravity
import androidx.annotation.IdRes
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val initSleepTime = 2000L

    @get:Rule
    var activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun checkInitializedState() {
        Thread.sleep(initSleepTime) // skip init requests

        // checkToolbarIsDisplayed
        onView(withId(R.id.cToolbar)).check(matches(isDisplayed()))

        // checkNavigationIsClosed
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)))

        // check default Fragment is showing
        onView(withId(R.id.fGitreposearchRoot)).check(matches(isDisplayed()))
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
        Thread.sleep(initSleepTime) // skip init requests

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

        // Check Left Drawer should be closed.
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
    }

    //todo: check progress bar is not showing?

}