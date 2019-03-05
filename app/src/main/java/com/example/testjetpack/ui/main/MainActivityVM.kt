package com.example.testjetpack.ui.main

import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.Profile
import com.example.testjetpack.ui.base.BaseViewModel
import javax.inject.Inject

class MainActivityVM : BaseViewModel() {

    @Inject
    lateinit var repository: IDataRepository

    init {
        MainApplication.component.inject(this)
    }

    val profile: MutableLiveData<Profile?> = MutableLiveData()

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