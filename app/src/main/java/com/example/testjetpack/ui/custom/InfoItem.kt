package com.example.testjetpack.ui.custom

import android.annotation.TargetApi
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.testjetpack.R
import kotlinx.android.synthetic.main.item_my_status_info_item.view.*

class InfoItem : ConstraintLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(21)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.item_my_status_info_item, this, true)
    }


    var value: Int
        get() = tvItemValue.text.toString().toInt()
        set(value) {
            tvItemValue.text = value.toString()
        }

    var info: CharSequence
        get() = tvItemInfo.text
        set(value) {
            tvItemInfo.text = value
        }
}