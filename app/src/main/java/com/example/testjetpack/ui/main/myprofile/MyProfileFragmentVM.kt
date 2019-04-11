package com.example.testjetpack.ui.main.myprofile

import androidx.lifecycle.LiveData
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.own.Profile
import com.example.testjetpack.ui.base.BaseViewModel
import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.livedata.Event
import com.example.testjetpack.utils.livedata.toMutableLiveData
import com.squareup.picasso.Picasso

class MyProfileFragmentVM(
    private val repository: IDataRepository,
    val picasso: Picasso
) : BaseViewModel<MyProfileFragmentVMEventStateChange>() {


    val profile: LiveData<Profile?>
        get() = _profile
    val emailOnEdit: LiveData<Boolean>
        get() = _emailOnEdit
    val phoneOnEdit: LiveData<Boolean>
        get() = _phoneOnEdit
    val emailAddr: MutableLiveData<String?> = MutableLiveData()
    val phoneNumber: MutableLiveData<String?> = MutableLiveData()
    val cardNumber: MutableLiveData<String?> = MutableLiveData()

    private val _profile: MutableLiveData<Profile?> = MutableLiveData()
    private val _emailOnEdit: MutableLiveData<Boolean> = false.toMutableLiveData()
    private val _phoneOnEdit: MutableLiveData<Boolean> = false.toMutableLiveData()

    fun editEmail() {
        val curEditing = _emailOnEdit.value!!
        _emailOnEdit.postValue(!curEditing)
        if (curEditing) {
            //todo: repository save edited value
        }
    }

    fun editPhone() {
        val curEditing = _phoneOnEdit.value!!
        _phoneOnEdit.postValue(!curEditing)
        if (curEditing) {
            //todo: repository save edited value
        }
    }

    fun addCard() {
        _events.value = Event(MyProfileFragmentVMEventStateChange.OpenCreditCardDetails)
    }

    fun getProfile() {
        processCallAsync(
            call = { repository.getProfile() },
            onSuccess = { profile ->
                _profile.postValue(profile)
            },
            onError = {
                _profile.postValue(null)
                onError(it)
            },
            showProgress = true
        )
    }
}

sealed class MyProfileFragmentVMEventStateChange : EventStateChange {
    object OpenCreditCardDetails : MyProfileFragmentVMEventStateChange()
}
