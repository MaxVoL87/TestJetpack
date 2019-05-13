package com.example.testjetpack.ui.main.mystatus

import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.livedata.toMutableLiveData


class MyStatusFragmentVM : BaseViewModel<MyStatusFragmentVMEventStateChange>() {

    val animationProgress = 0.0F.toMutableLiveData()

    val vpAdapter = MyStatusViewPagerAdapter().apply {
        itemViewModels = mutableListOf(
            ArcCharItemViewModel().apply {
                this@MyStatusFragmentVM.animationProgress.observeForever { this.setAnimationProgress(it) }
            },
            InfoItemViewModel()
        )
    }

}

sealed class MyStatusFragmentVMEventStateChange : EventStateChange