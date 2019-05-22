package com.example.testjetpack.ui.signin.passwordrecovery

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.R
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.isValidEmail
import com.example.testjetpack.utils.livedata.Event

class PasswordRecoveryFragmentVM(private val _resources: Resources) : BaseViewModel<PasswordRecoveryFragmentVMEventStateChange>() {

    private val _emailError = MutableLiveData<CharSequence>(null)

    val email = MutableLiveData<CharSequence>()

    val emailError: LiveData<CharSequence>
        get() = _emailError


    fun recover(){
        _emailError.value =
            if (email.value.isValidEmail()) null else _resources.getString(R.string.error_invalid_email)

        if (_emailError.value != null) {
            onError(Exception("Sending recovery email Error"))
            return
        }

        // todo: send recovery email

        _events.value = Event(PasswordRecoveryFragmentVMEventStateChange.OpenSignIn)
    }

}

sealed class PasswordRecoveryFragmentVMEventStateChange : EventStateChange {
    object OpenSignIn : PasswordRecoveryFragmentVMEventStateChange()
}