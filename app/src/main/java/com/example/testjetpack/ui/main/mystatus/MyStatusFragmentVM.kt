package com.example.testjetpack.ui.main.mystatus

import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange

class MyStatusFragmentVM : BaseViewModel<MyStatusFragmentVMEventStateChange>() {

}

sealed class MyStatusFragmentVMEventStateChange : EventStateChange