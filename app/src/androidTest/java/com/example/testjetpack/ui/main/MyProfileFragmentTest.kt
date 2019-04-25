package com.example.testjetpack.ui.main

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions.navigateTo
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.testjetpack.R
import com.example.testjetpack.ui.base.TestBase
import com.example.testjetpack.ui.custom.hasBackgroundColor
import com.example.testjetpack.ui.custom.withDrawableId
import com.example.testjetpack.utils.checkEditTextReady
import com.example.testjetpack.utils.checkLabel
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyProfileFragmentTest : TestBase() {

    // todo: implement with androidx.fragment:fragment-testing
    @get:Rule
    var activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    // todo: remove when implemented with androidx.fragment:fragment-testing
    override fun before() {
        super.before()

        // open drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())

        // Start the screen of fragment.
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_my_profile))

        Thread.sleep(2500) // skip fragment init
    }

    @Test
    fun checkInitializedState() {

        // Check that fragment was opened.
        onView(withId(R.id.fMyProfileRoot)).check(matches(isDisplayed()))

        // Check toolbar place holder is showing
        onView(withId(R.id.ivTopImage)).check(matches(isDisplayed()))

        onView(withId(R.id.ivProfilePicture)).check(matches(isDisplayed()))

        // check name
        checkLabel(R.id.tvName)
            .check(matches(not(withText("")))) // better to check with mock data https://android.jlelse.eu/espresso-tests-from-0-to-1-e5c32c8a595

        checkLabel(R.id.lDateOfBirth, R.string.date_of_birth)
        // check value DateOfBirth
        checkLabel(R.id.tvDateOfBirth)
            .check(matches(not(withText("")))) // better to check with mock data

        checkLabel(R.id.lDriverLicense, R.string.driver_license)
        // check value DriverLicense
        checkLabel(R.id.tvDriverLicense)
            .check(matches(not(withText("")))) // better to check with mock data

        checkLabel(R.id.lStateOfIssue, R.string.state_of_issue)
        // check value StateOfIssue
        checkLabel(R.id.tvStateOfIssue)
            .check(matches(not(withText("")))) // better to check with mock data

        // Check separator is displayed
        onView(withId(R.id.separator01))
            .check(matches(isDisplayed()))
            .check(matches(hasBackgroundColor(R.color.colorSeparator)))

        onView(withId(R.id.ivEmailPicture))
            .check(matches(isDisplayed()))
            .check(matches(withDrawableId(R.drawable.ic_email_48dp)))

        // check email edit as label
        checkLabel(R.id.etEmailAddress)
            .check(matches(withHint(R.string.hint_email)))

        onView(withId(R.id.ibEmailAddEdit))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(isClickable()))
            .check(matches(withDrawableId(R.drawable.ic_edit_36dp)))

        onView(withId(R.id.ivPhonePicture))
            .check(matches(isDisplayed()))
            .check(matches(withDrawableId(R.drawable.ic_phone_36dp)))

        // check email edit as label
        checkLabel(R.id.etPhoneNumber)
            .check(matches(withHint(R.string.hint_phone)))

        onView(withId(R.id.ibPhoneAddEdit))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(isClickable()))
            .check(matches(withDrawableId(R.drawable.ic_edit_36dp)))

        // check no card label, displayed by default
        checkLabel(R.id.lNoCard, R.string.you_currently_have_no_credit_card)

        // check card info, not displayed by default
        onView(withId(R.id.rlCardInfo))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.ivCard))
            .check(matches(not(isDisplayed())))
            .check(matches(withDrawableId(R.drawable.ic_credit_card_144dp)))

        onView(withId(R.id.tvCardNumber))
            .check(matches(not(isDisplayed())))
            .check(matches(not(withText("")))) // better to check with mock data

        onView(withId(R.id.bCardAddEdit))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(isClickable()))
    }

    @Test
    fun checkEmailEditing(){
        checkEdits(R.id.etEmailAddress, R.string.hint_email, "test@gmail.com",
            R.id.ibEmailAddEdit, R.drawable.ic_edit_36dp, R.drawable.ic_check_36dp)
    }

    @Test
    fun checkPhoneEditing(){
        checkEdits(R.id.etPhoneNumber, R.string.hint_phone, "1234567890",
            R.id.ibPhoneAddEdit, R.drawable.ic_edit_36dp, R.drawable.ic_check_36dp)
    }

    //todo: implement and add check for email/phone incompatible data entering

    private fun checkEdits(
        @IdRes editId: Int, @IdRes editHint: Int, editTextToEnter: String,
        @IdRes buttonId: Int, @IdRes buttonNotOnEditImage: Int, @IdRes buttonOnEditImage: Int){
        // check not in Edit sate
        checkLabel(editId)
            .check(matches(withHint(editHint)))
            .perform(click()) // click to check if field can be in edit state by click

        // check field not in edit state
        checkLabel(editId)
            .check(matches(withHint(editHint)))

        // check not in Edit sate
        onView(withId(buttonId))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(isClickable()))
            .check(matches(withDrawableId(buttonNotOnEditImage)))
            .perform(click())

        // check in Edit sate
        checkEditTextReady(editId)
            .check(matches(withHint(editHint)))
            .perform(typeText(editTextToEnter))

        // check button changed to edit state
        onView(withId(buttonId))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(isClickable()))
            .check(matches(withDrawableId(buttonOnEditImage)))
            .perform(click())

        // check not in Edit sate
        checkLabel(editId)
            .check(matches(withHint(editHint)))
            .check(matches(withText(editTextToEnter)))

        // check button changed to prev state
        onView(withId(buttonId))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(isClickable()))
            .check(matches(withDrawableId(buttonNotOnEditImage)))
    }
}