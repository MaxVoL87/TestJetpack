package com.example.testjetpack.ui.main

import android.view.KeyEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.testjetpack.R
import com.example.testjetpack.TestingContract.DEFAULT_UI_DELAY
import com.example.testjetpack.ui.base.TestBase
import com.example.testjetpack.ui.custom.*
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GitRepoSearchFragmentTest : TestBase() {

    // todo: implement with androidx.fragment:fragment-testing
    @get:Rule
    var activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun checkInitializedState() {
//        // todo: not working? E/TestRunner: java.lang.NoSuchMethodError: No static method getIntentForActivity(Landroidx/test/internal/platform/app/ActivityInvoker;Ljava/lang/Class;)Landroid/content/Intent; in class Landroidx/test/internal/platform/app/ActivityInvoker$$CC; or its super classes (declaration of 'androidx.test.internal.platform.app.ActivityInvoker$$CC' appears in /data/app/com.example.testjetpack-WmEdXYwxBc69SVdY6mVP9Q==/base.apk!classes3.dex)
//        val scenario = launchFragmentInContainer<GitRepoSearchFragment>()

        // Check that fragment was opened.
        onView(withId(R.id.fGitreposearchRoot)).check(matches(isDisplayed()))

        checkToolbarPlaceholder()

        onView(withId(R.id.etTextToSearch))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(isFocusable()))
            .check(matches(not(hasFocus()))) // no focus
            .check(matches(withHint(R.string.hint_repo_to_search)))
            .check(matches(withText("")))

        onView(withId(R.id.bSearch))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(isClickable()))
            .check(matches(withText(R.string.search)))

        onView(withId(R.id.srlRefresh))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))

        onView(withId(R.id.list))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(hasItemsCount(0))

    }

    @Test
    fun checkSearchByApply() {
        searchCheck { onView(withId(R.id.bSearch)).perform(pressKey(KeyEvent.KEYCODE_ENTER)) }
    }

    @Test
    fun checkSearchByClick() {
        searchCheck { onView(withId(R.id.bSearch)).perform(click()) }
    }


    private fun searchCheck(startSearchAction: () -> Unit) {
        onView(withId(R.id.etTextToSearch))
            .perform(typeText("testjetpack"))

        // check list is empty
        onView(withId(R.id.list))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(isEmpty()))

        startSearchAction()

        Thread.sleep(DEFAULT_UI_DELAY)

        // check 'searching/error' item or search result is displayed
        onView(withId(R.id.list))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(not(isEmpty())))
    }
}