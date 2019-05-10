package com.example.testjetpack.ui.main.mystatus

import com.example.testjetpack.ui.base.BaseRecyclerItemViewModel
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange


class MyStatusFragmentVM : BaseViewModel<MyStatusFragmentVMEventStateChange>() {

    val vpAdapter = MyStatusViewPagerAdapter().apply {
        itemViewModels = mutableListOf<BaseRecyclerItemViewModel>(ArcCharItemViewModel())
    }
}

sealed class MyStatusFragmentVMEventStateChange : EventStateChange