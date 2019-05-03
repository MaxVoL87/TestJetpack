package com.example.testjetpack.ui.main.mystatus

import androidx.lifecycle.LiveData
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.livedata.toMutableLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyStatusFragmentVM : BaseViewModel<MyStatusFragmentVMEventStateChange>() {

    private val _somePercents = 0.0F.toMutableLiveData()

    val somePercents: LiveData<Float>
        get() = _somePercents

    var maxPercents: Float = 0.8F

    fun calculatePercents() {

        launch {
            var percents = 0
            while (percents < maxPercents * 100) {
                delay(1000)
                percents += 10
                _somePercents.value = percents.div(100F)
            }
        }
    }
}

sealed class MyStatusFragmentVMEventStateChange : EventStateChange