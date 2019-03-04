package com.example.testjetpack.ui.main

import androidx.databinding.ObservableField
import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.Profile
import com.example.testjetpack.ui.base.BaseViewModel
import com.squareup.picasso.Picasso
import javax.inject.Inject

class MainActivityVM : BaseViewModel() {

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var repository: IDataRepository

    init {
        MainApplication.component.inject(this)
    }

    val profile: ObservableField<Profile?> = ObservableField()

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