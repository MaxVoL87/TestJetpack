package com.example.testjetpack.ui.signin.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentSignUpBinding
import com.example.testjetpack.ui.base.BaseFragmentWithCallback
import com.example.testjetpack.ui.base.EventStateChange
import kotlin.reflect.KClass

class SignUpFragment : BaseFragmentWithCallback<FragmentSignUpBinding, SignUpFragmentVM, ISignUpFragmentCallback>() {
    override val layoutId: Int = R.layout.fragment_sign_up
    override val viewModelClass: KClass<SignUpFragmentVM> = SignUpFragmentVM::class
    override val callbackClass: KClass<ISignUpFragmentCallback> = ISignUpFragmentCallback::class
    override val observeLiveData: SignUpFragmentVM.() -> Unit
        get() = {
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return view
    }

    // region VM events renderer
    private val openSignInRenderer: (Any) -> Unit = { event ->
        event as SignUpFragmentVMEventStateChange.OpenSignIn
        callback?.openSignIn()
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        SignUpFragmentVMEventStateChange.OpenSignIn::class to openSignInRenderer
    )

    // endregion VM events renderer
}
