package com.example.testjetpack.ui.main.myprofile

import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.Profile
import com.example.testjetpack.ui.base.BaseViewModel
import javax.inject.Inject
import androidx.lifecycle.MutableLiveData
import com.squareup.picasso.Picasso


class MyProfileFragmentVM : BaseViewModel() {

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var repository: IDataRepository

    init {
        MainApplication.component.inject(this)
    }

    val profile: MutableLiveData<Profile?> = MutableLiveData()

    val emailOnEdit: MutableLiveData<Boolean> = MutableLiveData(false)
    val phoneOnEdit: MutableLiveData<Boolean> = MutableLiveData(false)
    val emailAddr: MutableLiveData<String?> = MutableLiveData()
    val phoneNumber: MutableLiveData<String?> = MutableLiveData()

    val cardNumber: MutableLiveData<String?> = MutableLiveData()

    fun editEmail() {
        val curEditing = emailOnEdit.value!!
        emailOnEdit.postValue(!curEditing)
        if(curEditing){
            //todo: repository save edited value
        }
    }

    fun editPhone() {
        val curEditing = phoneOnEdit.value!!
        phoneOnEdit.postValue(!curEditing)
        if(curEditing){
            //todo: repository save edited value
        }
    }

    fun addCard() {
        //todo: open add card dialog?
    }

    fun getProfile() {
        processAsyncCall(
            call = { repository.getProfileAsync() },
            onSuccess = { profile ->
                this.profile.postValue(profile)
            },
            onError = {
                this.profile.postValue(null)
                onError(it)
            },
            showProgress = true
        )
    }

}