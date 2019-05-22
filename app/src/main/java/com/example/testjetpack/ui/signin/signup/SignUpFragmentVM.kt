package com.example.testjetpack.ui.signin.signup

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.R
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.isValidEmail
import com.example.testjetpack.utils.isValidPassword
import com.example.testjetpack.utils.livedata.Event
import com.example.testjetpack.utils.livedata.toMutableLiveData

class SignUpFragmentVM(private val _resources: Resources) : BaseViewModel<SignUpFragmentVMEventStateChange>() {

    private val _nameError = MutableLiveData<CharSequence>(null)
    private val _emailError = MutableLiveData<CharSequence>(null)
    private val _passwordError = MutableLiveData<CharSequence>(null)
    private val _confirmPasswordError = MutableLiveData<CharSequence>(null)
    private val _termsAcceptanceError = MutableLiveData<CharSequence>(null)

    val name = MutableLiveData<CharSequence>()
    val email = MutableLiveData<CharSequence>()
    val password = MutableLiveData<CharSequence>()
    val passwordConfirm = MutableLiveData<CharSequence>()
    val termsAccepted = false.toMutableLiveData()

    val nameError: LiveData<CharSequence>
        get() = _nameError

    val emailError: LiveData<CharSequence>
        get() = _emailError

    val passwordError: LiveData<CharSequence>
        get() = _passwordError

    val confirmPasswordError: LiveData<CharSequence>
        get() = _confirmPasswordError

    val termsAcceptanceError: LiveData<CharSequence>
        get() = _termsAcceptanceError

    fun register() {
        _nameError.value =
            if (!name.value.isNullOrBlank()) null else _resources.getString(R.string.error_field_required)
        _emailError.value =
            if (email.value.isValidEmail()) null else _resources.getString(R.string.error_invalid_email)
        _passwordError.value =
            if (password.value.isValidPassword()) null else _resources.getString(R.string.error_incorrect_password)
        _confirmPasswordError.value =
            if (password.value == passwordConfirm.value) null else _resources.getString(R.string.error_incorrect_password)
        _termsAcceptanceError.value =
            if (termsAccepted.value == true) null else _resources.getString(R.string.error_field_required)

        if (_nameError.value != null || _emailError.value != null || _passwordError.value != null || _confirmPasswordError.value != null || _termsAcceptanceError.value != null) {
            onError(Exception("Registration Error"))
            return
        }

        // todo: register

        _events.value = Event(SignUpFragmentVMEventStateChange.OpenSignIn)
    }
}

sealed class SignUpFragmentVMEventStateChange : EventStateChange {
    object OpenSignIn : SignUpFragmentVMEventStateChange()
}