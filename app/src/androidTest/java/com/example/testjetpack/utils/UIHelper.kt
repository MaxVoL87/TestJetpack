package com.example.testjetpack.utils

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.not

fun checkLabel(@IdRes id: Int) = onView(withId(id))
    .check(matches(isDisplayed()))
    .check(matches(isEnabled()))
    .check(matches(not(isFocusable())))

fun checkLabel(@IdRes id: Int, @IdRes text: Int) = checkLabel(id)
    .check(matches(withText(text)))

fun checkEditTextReady(@IdRes id: Int) = onView(withId(id))
    .check(matches(isDisplayed()))
    .check(matches(isEnabled()))
    .check(matches(isFocusable()))
    .check(matches(hasFocus()))