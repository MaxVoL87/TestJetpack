package com.example.testjetpack.utils.binding

import android.net.Uri
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testjetpack.utils.hideKeyboard
import com.example.testjetpack.utils.picasso.CircleTransform
import com.example.testjetpack.utils.showKeyboard
import com.squareup.picasso.Picasso


/**
 * Load and circle image
 */
@BindingAdapter("circleImageUrl", "picasso")
fun ImageView.setCircleImageUrl(imageUrl: String?, picasso: Picasso?) {
    if (imageUrl == null || picasso == null) return
    picasso.load(Uri.parse(imageUrl))
        .fit()
        .transform(CircleTransform())
        .into(this)
}

@BindingAdapter("onEditorActionListener")
fun TextView.onEditorActionListener(listener: TextView.OnEditorActionListener) {
    setOnEditorActionListener(listener)
}

/**
 * Invoke RecyclerView.scrollToPosition(position: Int) action
 */
@BindingAdapter("position")
fun RecyclerView.setPosition(position: Int) {
    scrollToPosition(position)
//    val offset = 10
//    val mLayoutManager = layoutManager
//    when(mLayoutManager){
//        is LinearLayoutManager -> mLayoutManager.scrollToPositionWithOffset(position, offset)
//        is StaggeredGridLayoutManager -> mLayoutManager.scrollToPositionWithOffset(position, offset)
//        else -> scrollToPosition(position)
//    }
}

/**
 * Show/Hide cursor, keyboard, focus
 */
@BindingAdapter("requestFocus")
fun EditText.requestFocus(requestFocus: Boolean) {
    isClickable = requestFocus
    isCursorVisible = requestFocus
    isFocusable = requestFocus
    isFocusableInTouchMode = requestFocus

    if (requestFocus) {
        requestFocus()
        showKeyboard()
    } else {
        hideKeyboard()
    }
}

@BindingAdapter("visibleOrGone")
fun View.setVisibleOrGone(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleOrInvisible")
fun View.setVisibleOrInvisible(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("onClick")
fun View.onClick(function: Runnable) {
    setOnClickListener { function.run() }
}
