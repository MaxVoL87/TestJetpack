package com.example.testjetpack.utils.binding

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testjetpack.R
import com.example.testjetpack.utils.hideKeyboard
import com.example.testjetpack.utils.picasso.CircleTransform
import com.example.testjetpack.utils.showKeyboard
import com.squareup.picasso.Picasso


/**
 * Load and circle image
 */
@BindingAdapter("text", "isAvailable", "notAvailableText", "changeColor")
fun TextView.setTextIfAvailable(text: String?, isAvailable: Boolean?, notAvailableText: String?, changeColor: Boolean) {
    val prevText = this.text
    val setText = if (isAvailable == true) {
        text
    } else {
        notAvailableText
    }

    if (prevText != setText) this.text = setText
    if (changeColor) {
        this.setTextColor(if (prevText != setText) Color.RED else Color.GRAY) //change to default
    }
}

/**
 * Load and circle image
 */
@BindingAdapter("circleImageUrl", "picasso")
fun ImageView.setCircleImageUrl(imageUrl: String?, picasso: Picasso?) {
    if (imageUrl == null || picasso == null) return
    picasso.load(Uri.parse(imageUrl))
        .placeholder(R.color.colorPrimary)
        .fit()
        .transform(CircleTransform())
        .into(this)
}

/**
 * Load image
 */
@BindingAdapter("picasso", "imageUrl", "placeholder", "centerCrop", requireAll = false)
fun ImageView.setImageUrl(picasso: Picasso?, imageUrl: String?, placeholder: Drawable?, centerCrop: Boolean?) {
    if (imageUrl == null || picasso == null) return
    picasso.load(Uri.parse(imageUrl))
        .apply { if (placeholder == null) placeholder(R.color.colorPrimary) else placeholder(placeholder) }
        .fit()
        .apply {
            if (centerCrop == true) {
                noFade()
                centerCrop()
            }
        }
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
    if (position < 0) return

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
 * Invoke RecyclerView.scrollToPosition(position: Int) action
 */
@BindingAdapter("map")
fun ListView.setMap(map: Map<String, String>?) {
    if (map == null) return

    val name = "name"
    val value = "value"
    val data = map.map { hashMapOf(name to it.key, value to it.value) }
    val from = arrayOf(name, value)
    val to = arrayOf(android.R.id.text1, android.R.id.text2).toIntArray()

    adapter = SimpleAdapter(context, data, android.R.layout.simple_expandable_list_item_2, from, to)
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
    val vsb = if (show) View.VISIBLE else View.GONE
    if (visibility != vsb) visibility = vsb
}

@BindingAdapter("visibleOrInvisible")
fun View.setVisibleOrInvisible(show: Boolean) {
    val vsb = if (show) View.VISIBLE else View.INVISIBLE
    if (visibility != vsb) visibility = vsb
}

@BindingAdapter("onClick")
fun View.onClick(function: Runnable) {
    setOnClickListener { function.run() }
}

@BindingAdapter("error")
fun TextView.setError(error: CharSequence?) {
    if (this.error != error) this.error = error
}

@InverseBindingAdapter(attribute = "error")
fun TextView.getError(): CharSequence? {
    return error
}