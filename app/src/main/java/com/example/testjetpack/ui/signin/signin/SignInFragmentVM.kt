package com.example.testjetpack.ui.signin.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.isValidEmail
import com.example.testjetpack.utils.isValidPassword
import com.example.testjetpack.utils.livedata.Event
import java.lang.Exception
import kotlin.random.Random

class SignInFragmentVM : BaseViewModel<SignInFragmentVMEventStateChange>() {

    private val _emailError = MutableLiveData<CharSequence>(null)
    private val _passwordError = MutableLiveData<CharSequence>(null)

    val email = MutableLiveData<CharSequence>()
    val password = MutableLiveData<CharSequence>()

    val emailError: LiveData<CharSequence>
        get() = _emailError

    val passwordError: LiveData<CharSequence>
        get() = _passwordError

    fun signIn() {
        _emailError.value = if (email.value.isValidEmail()) null else "Email not valid"
        _passwordError.value = if (password.value.isValidPassword()) null else "Password not valid"

// todo: uncommit and remove last login()
//        if (_emailError.value == null && _passwordError.value == null) login()
//        else onError(Exception("Login Failed"))

        login()
    }

    fun signInWithFacebook() {
        login()
    }

    fun signInWithTwitter() {
        login()
    }

    fun passwordRecovery() {
        _events.value = Event(SignInFragmentVMEventStateChange.OpenPasswordRecovery)
    }

    fun register() {
        _events.value = Event(SignInFragmentVMEventStateChange.OpenRegistration)
    }

    private fun login() {
        processCallAsync(
            call = {
                val toValue = Random.nextInt(2, 6)
                for (i in 0..toValue) {
                    Thread.sleep(1000)
                    if (i == toValue && i % 2 != 0) throw Exception("Login Failed")
                }
            },
            onSuccess = {
                _events.value = Event(SignInFragmentVMEventStateChange.OpenMain)
            },
            onError = {
                onError(it)
            },
            showProgress = true
        )
    }
}

sealed class SignInFragmentVMEventStateChange : EventStateChange {
    object OpenMain : SignInFragmentVMEventStateChange()
    object OpenRegistration : SignInFragmentVMEventStateChange()
    object OpenPasswordRecovery : SignInFragmentVMEventStateChange()
}