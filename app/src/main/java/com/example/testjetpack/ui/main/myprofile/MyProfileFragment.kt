package com.example.testjetpack.ui.main.myprofile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testjetpack.databinding.FragmentMyProfileBinding
import com.example.testjetpack.ui.base.EventStateChange
import kotlin.reflect.KClass
import com.example.testjetpack.R
import com.example.testjetpack.ui.base.BaseFragmentWithCallback


class MyProfileFragment : BaseFragmentWithCallback<FragmentMyProfileBinding, MyProfileFragmentVM, IMyProfileFragmentCallback>() {
    override val layoutId: Int = R.layout.fragment_my_profile
    override val viewModelClass: KClass<MyProfileFragmentVM> = MyProfileFragmentVM::class
    override val callbackClass: KClass<IMyProfileFragmentCallback> = IMyProfileFragmentCallback::class
    override val observeLiveData: MyProfileFragmentVM.() -> Unit
        get() = {
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return view
    }

    override fun onStart() {
        super.onStart()
        viewModel.getProfile()
    }

    // region VM renderers

    private val openCreditCardDetailsRenderer: (Any) -> Unit = { event ->
        event as MyProfileFragmentVMEventStateChange.OpenCreditCardDetails
        callback?.openCreditCardDetails()
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        MyProfileFragmentVMEventStateChange.OpenCreditCardDetails::class to openCreditCardDetailsRenderer
    )
    // endregion VM renderers
}
