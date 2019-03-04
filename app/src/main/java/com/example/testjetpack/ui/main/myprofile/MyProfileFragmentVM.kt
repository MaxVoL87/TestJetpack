package com.example.testjetpack.ui.main.myprofile

import androidx.databinding.ObservableBoolean
import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.Profile
import com.example.testjetpack.ui.base.BaseViewModel
import javax.inject.Inject
import androidx.databinding.ObservableField
import com.squareup.picasso.Picasso


class MyProfileFragmentVM : BaseViewModel() {

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var repository: IDataRepository

    init {
        MainApplication.component.inject(this)
    }

    val profile: ObservableField<Profile?> = ObservableField()

    val emailOnEdit: ObservableBoolean = ObservableBoolean(false)
    val phoneOnEdit: ObservableBoolean = ObservableBoolean(false)
    val emailAddr: ObservableField<String?> = ObservableField()
    val phoneNumber: ObservableField<String?> = ObservableField()

    val cardNumber: ObservableField<String?> = ObservableField()

    fun editEmail() {
        val curEditing = emailOnEdit.get()
        emailOnEdit.set(!curEditing)
        if(curEditing){
            //todo: repository save edited value
        }
    }

    fun editPhone() {
        val curEditing = phoneOnEdit.get()
        phoneOnEdit.set(!curEditing)
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
                this.profile.set(profile)
            },
            onError = {
                this.profile.set(null)
                onError(it)
            },
            showProgress = true
        )
    }

}