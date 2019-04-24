package com.example.testjetpack.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions.navigateTo
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.testjetpack.R
import com.example.testjetpack.ui.base.TestBase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GitRepoSearchFragmentTest : TestBase() {

    // todo: implement with androidx.fragment:fragment-testing
    @get:Rule
    var activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    // todo: remove when implemented with androidx.fragment:fragment-testing
    override fun setUp() {
        super.setUp()

        // open drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())

        // Start the screen of fragment.
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_my_profile))
    }

    @Test
    fun checkInitializedState() {
//        // todo: not working? E/TestRunner: java.lang.NoSuchMethodError: No static method getIntentForActivity(Landroidx/test/internal/platform/app/ActivityInvoker;Ljava/lang/Class;)Landroid/content/Intent; in class Landroidx/test/internal/platform/app/ActivityInvoker$$CC; or its super classes (declaration of 'androidx.test.internal.platform.app.ActivityInvoker$$CC' appears in /data/app/com.example.testjetpack-WmEdXYwxBc69SVdY6mVP9Q==/base.apk!classes3.dex)
//        val scenario = launchFragmentInContainer<GitRepoSearchFragment>()



        // Check that fragment was opened.
        onView(withId(R.id.fGitreposearchRoot)).check(matches(isDisplayed()))
    }

}