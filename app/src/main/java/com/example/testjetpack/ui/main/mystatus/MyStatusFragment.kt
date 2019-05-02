package com.example.testjetpack.ui.main.mystatus


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentMyStatusBinding
import com.example.testjetpack.ui.base.BaseFragmentWithCallback
import com.example.testjetpack.ui.base.EventStateChange
import kotlin.reflect.KClass

/**
 * A simple [Fragment] subclass.
 *
 */
class MyStatusFragment : BaseFragmentWithCallback<FragmentMyStatusBinding, MyStatusFragmentVM, IMyStatusFragmentCallback>() {
    override val layoutId: Int = R.layout.fragment_my_status
    override val viewModelClass: KClass<MyStatusFragmentVM> = MyStatusFragmentVM::class
    override val callbackClass: KClass<IMyStatusFragmentCallback> = IMyStatusFragmentCallback::class
    override val observeLiveData: MyStatusFragmentVM.() -> Unit
        get() = {
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return view
    }

    // region VM events renderer

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
    )

    // endregion VM events renderer
}
