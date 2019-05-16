package com.example.testjetpack.ui.dialog.message

import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.livedata.Event
import com.example.testjetpack.utils.livedata.toMutableLiveData

class MessageDialogFragmentVM : BaseViewModel<MessageDialogFragmentVMEventStateChange>() {

    val header = MutableLiveData<String>()
    val text = MutableLiveData<String>()
    val isCancelable = true.toMutableLiveData()

    fun bAction1OnClick(){
        _events.value = Event(MessageDialogFragmentVMEventStateChange.Close)
        _events.value = Event(MessageDialogFragmentVMEventStateChange.Ok)
    }

    fun bAction2OnClick(){
        _events.value = Event(MessageDialogFragmentVMEventStateChange.Close)
        _events.value = Event(MessageDialogFragmentVMEventStateChange.Cancel)
    }
}

sealed class MessageDialogFragmentVMEventStateChange : EventStateChange {
    object Ok : MessageDialogFragmentVMEventStateChange()
    object Cancel : MessageDialogFragmentVMEventStateChange()
    object Close : MessageDialogFragmentVMEventStateChange()
}