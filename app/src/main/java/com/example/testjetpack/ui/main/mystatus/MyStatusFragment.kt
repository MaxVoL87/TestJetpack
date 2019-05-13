package com.example.testjetpack.ui.main.mystatus


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2

import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentMyStatusBinding
import com.example.testjetpack.ui.base.BaseFragmentWithCallback
import com.example.testjetpack.ui.base.EventStateChange
import kotlinx.android.synthetic.main.fragment_my_status.*
import kotlin.reflect.KClass

/**
 * A simple [Fragment] subclass.
 *
 */
class MyStatusFragment :
    BaseFragmentWithCallback<FragmentMyStatusBinding, MyStatusFragmentVM, IMyStatusFragmentCallback>() {
    override val layoutId: Int = R.layout.fragment_my_status
    override val viewModelClass: KClass<MyStatusFragmentVM> = MyStatusFragmentVM::class
    override val callbackClass: KClass<IMyStatusFragmentCallback> = IMyStatusFragmentCallback::class
    override val observeLiveData: MyStatusFragmentVM.() -> Unit
        get() = {
        }


    private val onPageChangedCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            val numberOfPages = vpRoot.adapter?.itemCount
            if (numberOfPages != null && numberOfPages > 1) {
                val newProgress = (position + positionOffset) / (numberOfPages - 1)
                viewModel.animationProgress.value = newProgress
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return view
    }

    override fun onStart() {
        super.onStart()
        vpRoot.registerOnPageChangeCallback(onPageChangedCallback)
    }

    override fun onStop() {
        super.onStop()
        vpRoot.unregisterOnPageChangeCallback(onPageChangedCallback)
    }

    // region VM events renderer

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
    )

    // endregion VM events renderer

}
