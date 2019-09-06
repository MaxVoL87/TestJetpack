package com.example.testjetpack.ui.signin.signin

import com.example.testjetpack.ui.base.IBaseCallback

interface ISignInFragmentCallback : IBaseCallback {
    fun openMain()
    fun openPasswordRecovery()
    fun openRegistration()
}