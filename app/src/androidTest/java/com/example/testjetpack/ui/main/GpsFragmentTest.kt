package com.example.testjetpack.ui.main

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.testjetpack.R
import com.example.testjetpack.ui.base.TestBase
import com.example.testjetpack.ui.custom.hasBackgroundColor
import com.example.testjetpack.ui.custom.withDrawableId
import com.example.testjetpack.utils.checkLabel
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GpsFragmentTest : TestBase() {

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
            .perform(NavigationViewActions.navigateTo(R.id.nav_gps))

        Thread.sleep(500) // skip fragment init
    }

    @Test
    fun checkInitializedState() {
        // Check that fragment was opened.
        onView(withId(R.id.fGPSRoot))
            .check(matches(isDisplayed()))

        checkToolbarPlaceholder()

        checkToolbarItemListenerState()

        // check toolbar menu item GPS only displayed and clickable
        checkToolbarItemGPSOnly()

        // check toolbar menu item diagnostic displayed and clickable
        onView(withText(R.string.menu_item_diagnostic))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(isClickable()))

        // check toolbar menu item clear locations hidden
        onView(withText(R.string.menu_item_clear_locations)).check(doesNotExist())

        checkLabel(R.id.lLatitude, R.string.latitude__)
        // check value Latitude
        checkLabel(R.id.tvLatitude).check(matches(withText(R.string.no_value)))
            .check(matches(hasTextColor(R.color.colorRed)))

        checkLabel(R.id.lLongitude, R.string.longitude__)
        checkLabel(R.id.tvLongitude).check(matches(withText(R.string.no_value)))
            .check(matches(hasTextColor(R.color.colorRed)))

        checkLabel(R.id.lAltitude, R.string.altitude__)
        checkLabel(R.id.tvAltitude).check(matches(withText(R.string.no_value)))
            .check(matches(hasTextColor(R.color.colorRed)))

        checkLabel(R.id.lBearing, R.string.bearing__)
        checkLabel(R.id.tvBearing).check(matches(withText(R.string.no_value)))
            .check(matches(hasTextColor(R.color.colorRed)))

        checkLabel(R.id.lSpeed, R.string.speed__)
        checkLabel(R.id.tvSpeed).check(matches(withText(R.string.no_value)))
            .check(matches(hasTextColor(R.color.colorRed)))

        checkLabel(R.id.lAccuracy, R.string.accuracy__)
        checkLabel(R.id.tvAccuracy).check(matches(withText(R.string.no_value)))
            .check(matches(hasTextColor(R.color.colorRed)))

        checkLabel(R.id.lVerticalAccuracy, R.string.vertical_accuracy__)
        checkLabel(R.id.tvVerticalAccuracy).check(matches(withText(R.string.no_value)))
            .check(matches(hasTextColor(R.color.colorRed)))

        checkLabel(R.id.lSpeedAccuracy, R.string.speed_accuracy__)
        checkLabel(R.id.tvSpeedAccuracy).check(matches(withText(R.string.no_value)))
            .check(matches(hasTextColor(R.color.colorRed)))

        checkLabel(R.id.lBearingAccuracy, R.string.bearing_accuracy__)
        checkLabel(R.id.tvBearingAccuracy).check(matches(withText(R.string.no_value)))
            .check(matches(hasTextColor(R.color.colorRed)))

        checkLabel(R.id.lTime, R.string.time__millis__)
        checkLabel(R.id.tvTime).check(matches(withText(R.string.no_value)))
            .check(matches(hasTextColor(R.color.colorRed)))

        checkLabel(R.id.lElapsedRealTime, R.string.elapsed_real_time__)
        checkLabel(R.id.tvElapsedRealTime).check(matches(withText(R.string.no_value)))
            .check(matches(hasTextColor(R.color.colorRed)))

        // Check separator is displayed
        onView(withId(R.id.separator))
            .check(matches(isDisplayed()))
            .check(matches(hasBackgroundColor(R.color.colorSeparator)))

        checkLabel(R.id.lAcceleration, R.string.acceleration__)
        checkLabel(R.id.tvAcceleration).check(matches(withText(R.string.no_value)))
            .check(matches(hasTextColor(R.color.colorRed)))

        checkLabel(R.id.lSatellites, R.string.satellites__)
        checkLabel(R.id.tvSatellites).check(matches(withText(R.string.no_value)))
            .check(matches(hasTextColor(R.color.colorRed)))

        // Check button start/stop location listener
        checkStartStopLocationListenerButton()
    }

    @Test
    fun checkHiddenToolbarMenuItems() {
        // check toolbar menu item clear locations hidden
        onView(withText(R.string.menu_item_clear_locations)).check(doesNotExist())

        // show Options menu
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)

        // check toolbar menu item clear locations displayed and clickable
        onView(withText(R.string.menu_item_clear_locations))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
    }

    @Test
    fun checkStartStopListener() {
        // check listener state item displayed
        checkToolbarItemListenerState()

        // check toolbar menu item GPS only displayed and clickable
        checkToolbarItemGPSOnly()

        // check button start/stop location listener & click
        checkStartStopLocationListenerButton()
            .perform(click())

        Thread.sleep(500)

        // check listener state item changed state
        onView(withId(R.id.isListenerStarted))
            .check(matches(isDisplayed()))
            .check(matches(withDrawableId(R.drawable.ic_gps_fixed_red_48dp)))

        // check toolbar menu item GPS only is disabled
        onView(withText(R.string.menu_item_gps_only))
            .check(matches(isDisplayed()))
            .check(matches(not(isEnabled())))

        // check button start/stop location changed text & click
        onView(withId(R.id.bStartStopLocationListener))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(isClickable()))
            .check(matches(withText(R.string.location_listener_stop)))
            .perform(click())

        Thread.sleep(500)

        // check listener state item  returned to prev state
        checkToolbarItemListenerState()

        // check toolbar menu item GPS only returned to prev state
        checkToolbarItemGPSOnly()

        // check button start/stop location to prev state
        checkStartStopLocationListenerButton()
    }


    private fun checkToolbarItemListenerState(): ViewInteraction {
        return onView(withId(R.id.isListenerStarted))
            .check(matches(isDisplayed()))
            .check(matches(withDrawableId(R.drawable.ic_gps_not_fixed_grey_48dp)))
    }

    private fun checkToolbarItemGPSOnly(): ViewInteraction {
        return onView(withText(R.string.menu_item_gps_only))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(isClickable()))
    }

    private fun checkStartStopLocationListenerButton(): ViewInteraction {
        return onView(withId(R.id.bStartStopLocationListener))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(isClickable()))
            .check(matches(withText(R.string.location_listener_start)))
    }
}