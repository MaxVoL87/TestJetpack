package com.example.testjetpack.utils.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun Any.toLDString(): LiveData<String> = toMLDString()
fun Any.toMLDString() = MutableLiveData(toString())


fun <T> T.toLiveData(): LiveData<T> = toMutableLiveData()
fun <T> T.toMutableLiveData() = MutableLiveData<T>(this)

fun <T> MutableLiveData<T>.setIfNotTheSame(newValue: T?) {
    if (value != newValue) value = newValue
}