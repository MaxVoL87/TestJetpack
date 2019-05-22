package com.example.testjetpack.ui.signin.passwordrecovery


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentPasswordRecoveryBinding
import com.example.testjetpack.ui.base.BaseFragmentWithCallback
import com.example.testjetpack.ui.base.EventStateChange
import kotlin.reflect.KClass


class PasswordRecoveryFragment : BaseFragmentWithCallback<FragmentPasswordRecoveryBinding, PasswordRecoveryFragmentVM, IPasswordRecoveryFragmentCallback>() {
    override val layoutId: Int = R.layout.fragment_password_recovery
    override val viewModelClass: KClass<PasswordRecoveryFragmentVM> = PasswordRecoveryFragmentVM::class
    override val callbackClass: KClass<IPasswordRecoveryFragmentCallback> = IPasswordRecoveryFragmentCallback::class
    override val observeLiveData: PasswordRecoveryFragmentVM.() -> Unit
        get() = {
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return view
    }

    // region VM events renderer
    private val openSignInRenderer: (Any) -> Unit = { event ->
        event as PasswordRecoveryFragmentVMEventStateChange.OpenSignIn
        callback?.openSignIn()
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        PasswordRecoveryFragmentVMEventStateChange.OpenSignIn::class to openSignInRenderer
    )

    // endregion VM events renderer
}
