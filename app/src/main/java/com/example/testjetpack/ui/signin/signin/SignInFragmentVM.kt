package com.example.testjetpack.ui.signin.signin

import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange

class SignInFragmentVM : BaseViewModel<SignInFragmentVMEventStateChange>() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()


    fun signIn() {
    }

    fun signInWithFacebook() {
    }

    fun signInWithTwitter() {
    }
}

sealed class SignInFragmentVMEventStateChange : EventStateChange {
}