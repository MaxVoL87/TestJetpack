package com.example.testjetpack.ui.main.myprofile


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testjetpack.databinding.FragmentMyProfileBinding
import com.example.testjetpack.ui.base.BaseFragment
import com.example.testjetpack.ui.base.EventStateChange
import kotlin.reflect.KClass
import com.example.testjetpack.R


class MyProfileFragment : BaseFragment<FragmentMyProfileBinding, MyProfileFragmentVM>() {
    override val layoutId: Int = R.layout.fragment_my_profile
    override val viewModelClass: KClass<MyProfileFragmentVM> = MyProfileFragmentVM::class
    override val observeLiveData: MyProfileFragmentVM.() -> Unit
        get() = {
        }

    private var _callback: IMyProfileFragmentCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _callback = bindInterfaceOrThrow<IMyProfileFragmentCallback>(parentFragment, context)
    }

    override fun onDetach() {
        _callback = null
        super.onDetach()
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
        _callback?.openCreditCardDetails()
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        MyProfileFragmentVMEventStateChange.OpenCreditCardDetails::class to openCreditCardDetailsRenderer
    )
    // endregion VM renderers
}
