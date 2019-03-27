package com.example.testjetpack.ui.main.myprofile

import androidx.lifecycle.LiveData
import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.own.Profile
import com.example.testjetpack.ui.base.BaseViewModel
import javax.inject.Inject
import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.livedata.Event
import com.squareup.picasso.Picasso


class MyProfileFragmentVM : BaseViewModel<MyProfileFragmentVMEventStateChange>() {

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var repository: IDataRepository

    init {
        MainApplication.component.inject(this)
    }

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
    private val _emailOnEdit: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _phoneOnEdit: MutableLiveData<Boolean> = MutableLiveData(false)

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
            call = { repository.getProfileAsync() },
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
