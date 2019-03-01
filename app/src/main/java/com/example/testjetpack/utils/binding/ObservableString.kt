package com.example.testjetpack.utils.binding

import androidx.databinding.BaseObservable


class ObservableString() : BaseObservable() {

    private var value: String? = ""

    val isEmpty: Boolean
        get() = value == null || value!!.isEmpty()

    constructor(value: String) : this() {
        this.value = value
    }


    fun get(): String? {
        return if (value != null) value else ""
    }

    fun set(value: String?) {
        var pValue = value
        if (pValue == null) pValue = ""
        if (!this.value!!.contentEquals(pValue)) {
            this.value = pValue
            notifyChange()
        }
    }

    fun clear() {
        set(null)
    }
}