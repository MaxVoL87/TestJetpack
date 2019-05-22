package com.example.testjetpack.ui.signin.signin

import com.example.testjetpack.ui.base.ICallback

interface ISignInFragmentCallback : ICallback {
    fun openMain()
    fun openPasswordRecovery()
    fun openRegistration()
}