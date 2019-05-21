package com.example.testjetpack.ui.signin.signin


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentSignInBinding
import com.example.testjetpack.ui.base.BaseFragmentWithCallback
import com.example.testjetpack.ui.base.EventStateChange
import kotlin.reflect.KClass

class SignInFragment : BaseFragmentWithCallback<FragmentSignInBinding, SignInFragmentVM, ISignInFragmentCallback>(){
    override val layoutId: Int = R.layout.fragment_sign_in
    override val viewModelClass: KClass<SignInFragmentVM> = SignInFragmentVM::class
    override val callbackClass: KClass<ISignInFragmentCallback> = ISignInFragmentCallback::class
    override val observeLiveData: SignInFragmentVM.() -> Unit
        get() = {
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return view
    }

    // region VM events renderer
    private val openMainRenderer: (Any) -> Unit = { event ->
        event as SignInFragmentVMEventStateChange.OpenMain
        callback?.openMain()
    }

    private val openPasswordRecoveryRenderer: (Any) -> Unit = { event ->
        event as SignInFragmentVMEventStateChange.OpenPasswordRecovery
        callback?.openPasswordRecovery()
    }

    private val openRegistrationRenderer: (Any) -> Unit = { event ->
        event as SignInFragmentVMEventStateChange.OpenRegistration
        callback?.openRegistration()
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        SignInFragmentVMEventStateChange.OpenMain::class to openMainRenderer,
        SignInFragmentVMEventStateChange.OpenPasswordRecovery::class to openPasswordRecoveryRenderer,
        SignInFragmentVMEventStateChange.OpenRegistration::class to openRegistrationRenderer
    )

    // endregion VM events renderer
}
