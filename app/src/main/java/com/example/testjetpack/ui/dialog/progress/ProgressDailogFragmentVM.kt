package com.example.testjetpack.ui.dialog.progress

import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.livedata.toMutableLiveData

class ProgressDialogFragmentVM : BaseViewModel<ProgressDialogFragmentVMEventStateChange>() {

    val text = MutableLiveData<String>()
    val isProgressBarVisible = true.toMutableLiveData()
    val isCancelable = true.toMutableLiveData()
}

sealed class ProgressDialogFragmentVMEventStateChange : EventStateChange {
    object Close : ProgressDialogFragmentVMEventStateChange()
}