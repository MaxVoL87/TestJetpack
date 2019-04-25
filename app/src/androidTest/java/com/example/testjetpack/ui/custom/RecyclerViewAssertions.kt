package com.example.testjetpack.ui.custom

import android.view.View
import android.view.View.FIND_VIEWS_WITH_TEXT
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


fun hasItemsCount(count: Int): ViewAssertion = RecyclerViewItemCountAssertion(count)

fun hasHolderItemAtPosition(index: Int, viewHolderMatcher: Matcher<RecyclerView.ViewHolder>): ViewAssertion =
    RecyclerViewHolderItemAtPositionAssertion(index, viewHolderMatcher)

fun hasViewWithTextAtPosition(index: Int, text: CharSequence): ViewAssertion =
    RecyclerViewViewWithTextAtPositionAssertion(index, text)

fun hasViewWithText(text: CharSequence): ViewAssertion =
    RecyclerViewViewWithTextAssertion(text)

fun isEmpty(): Matcher<View> {

    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("recycler is empty")
        }

        override fun matchesSafely(view: View?): Boolean {
            if (view !is RecyclerView) {
                throw IllegalStateException("The asserted view is not RecyclerView")
            }

            val adapter = view.adapter ?: throw IllegalStateException("No adapter is assigned to RecyclerView")

            return adapter.itemCount == 0
        }
    }
}

private class RecyclerViewItemCountAssertion(private val count: Int) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        if (view !is RecyclerView) {
            throw IllegalStateException("The asserted view is not RecyclerView")
        }

        val adapter = view.adapter ?: throw IllegalStateException("No adapter is assigned to RecyclerView")

        ViewMatchers.assertThat("RecyclerView item count", adapter.itemCount, CoreMatchers.equalTo(count))
    }
}

private class RecyclerViewHolderItemAtPositionAssertion(
    private val index: Int,
    private val viewHolderMatcher: Matcher<RecyclerView.ViewHolder>
) : ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        if (view !is RecyclerView) {
            throw IllegalStateException("The asserted view is not RecyclerView")
        }

        ViewMatchers.assertThat(view.findViewHolderForAdapterPosition(index), viewHolderMatcher)
    }
}

private class RecyclerViewViewWithTextAtPositionAssertion(
    private val index: Int,
    private val text: CharSequence
) : ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        if (view !is RecyclerView) {
            throw IllegalStateException("The asserted view is not RecyclerView")
        }

        val outviews = ArrayList<View>()
        view.findViewHolderForAdapterPosition(index)!!.itemView.findViewsWithText(
            outviews, text,
            FIND_VIEWS_WITH_TEXT
        )
        check(outviews.isNotEmpty()) { "There's no view at index $index of recyclerview that has text : $text" }
    }
}

private class RecyclerViewViewWithTextAssertion(private val text: CharSequence) : ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        if (view !is RecyclerView) {
            throw IllegalStateException("The asserted view is not RecyclerView")
        }

        val outviews = ArrayList<View>()
        for (index in 0 until view.adapter!!.itemCount) {
            view.findViewHolderForAdapterPosition(index)!!.itemView.findViewsWithText(
                outviews, text,
                FIND_VIEWS_WITH_TEXT
            )
            if (outviews.size > 0) break
        }
        check(outviews.isNotEmpty()) { "There's no view at recyclerview that has text : $text" }
    }
}