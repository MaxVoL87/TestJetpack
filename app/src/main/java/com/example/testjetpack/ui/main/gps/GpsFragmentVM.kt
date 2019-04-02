package com.example.testjetpack.ui.main.gps

import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange

class GpsFragmentVM : BaseViewModel<GpsFragmentVMEventStateChange>() {

}

sealed class GpsFragmentVMEventStateChange : EventStateChange {
}