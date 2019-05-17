package com.example.testjetpack.ui.signin

import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange

class SignInActivityVM : BaseViewModel<SignInActivityVMEventStateChange>(){

}

sealed class SignInActivityVMEventStateChange : EventStateChange {
}