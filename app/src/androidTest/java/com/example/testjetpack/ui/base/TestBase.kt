package com.example.testjetpack.ui.base

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.testjetpack.R
import com.example.testjetpack.TestingContract.START_DELAY_MILLIS
import com.example.testjetpack.ui.custom.hasBackgroundColor
import org.junit.Before


abstract class TestBase {

    @Before
    open fun before() {
        Thread.sleep(START_DELAY_MILLIS)
    }

    protected fun checkToolbarPlaceholder(): ViewInteraction {
        // Check toolbar place holder is showing
        return onView(withId(R.id.toolbarPlaceHolder))
            .check(matches(isDisplayed()))
            .check(matches(hasBackgroundColor(R.color.colorPrimary)))
    }

}