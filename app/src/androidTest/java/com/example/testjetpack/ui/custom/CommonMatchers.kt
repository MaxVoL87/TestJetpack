package com.example.testjetpack.ui.custom

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import androidx.test.internal.util.Checks
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat


fun hasBackgroundColor(colorRes: Int): Matcher<View> {
    Checks.checkNotNull(colorRes)

    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("background color: $colorRes")
        }

        override fun matchesSafely(item: View?): Boolean {

            val context = item?.context
            val textViewColor = (item?.background as ColorDrawable).color
            val expectedColor = if (Build.VERSION.SDK_INT <= 22) {
                context?.resources?.getColor(colorRes)
            } else {
                context?.getColor(colorRes)
            }

            return textViewColor == expectedColor
        }
    }
}

fun withDrawableId(@DrawableRes expectedId: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>(View::class.java) {
        private var resourceName: String? = null

        override fun matchesSafely(target: View): Boolean {
            if (target !is ImageView) {
                return false
            }
            if (expectedId < 0) {
                return target.drawable == null
            }
            val expectedDrawable = ContextCompat.getDrawable(target.context, expectedId)
            resourceName = target.context.resources.getResourceEntryName(expectedId)
            return if (expectedDrawable != null && expectedDrawable.constantState != null) {
                expectedDrawable.constantState == target.drawable.constantState
            } else {
                false
            }
        }


        override fun describeTo(description: Description) {
            description.appendText("with drawable from resource id: ")
            description.appendValue(expectedId)
            if (resourceName != null) {
                description.appendText("[")
                description.appendText(resourceName)
                description.appendText("]")
            }
        }
    }
}