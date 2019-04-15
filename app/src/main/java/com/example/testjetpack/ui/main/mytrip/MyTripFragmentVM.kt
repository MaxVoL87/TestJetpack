package com.example.testjetpack.ui.main.mytrip

import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange

class MyTripFragmentVM : BaseViewModel<MyTripFragmentVMEventStateChange>() {


}

sealed class MyTripFragmentVMEventStateChange : EventStateChange {
}