package com.example.testjetpack.ui.main.mystatus


import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.lifecycle.Observer

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
class MyStatusFragment : BaseFragmentWithCallback<FragmentMyStatusBinding, MyStatusFragmentVM, IMyStatusFragmentCallback>() {
    override val layoutId: Int = R.layout.fragment_my_status
    override val viewModelClass: KClass<MyStatusFragmentVM> = MyStatusFragmentVM::class
    override val callbackClass: KClass<IMyStatusFragmentCallback> = IMyStatusFragmentCallback::class
    override val observeLiveData: MyStatusFragmentVM.() -> Unit
        get() = {
            somePercents.observe(this@MyStatusFragment, Observer {
                cloudAnimator.cancel()
                cloudScaleAnimListener.setTo(it)

                cloudAnimator.start()
            })
        }


    private val cloudScaleAnimListener = object : ValueAnimator.AnimatorUpdateListener {
        private var cloudScale = 0.0F
        private var cloudScaleTo = 0.0F

        fun setTo(to: Float){
            if(to > 1F) throw IllegalArgumentException("to > 1F")
            cloudScale = cloudScaleTo
            cloudScaleTo = to
        }

        override fun onAnimationUpdate(it: ValueAnimator) {
            val value = it.animatedFraction
            cloudScale = cloudScale * (1 - value) + cloudScaleTo * value
            cloud.scaleX = cloudScale
            cloud.scaleY = cloudScale
        }
    }
    private val cloudAnimator: ValueAnimator = ValueAnimator.ofFloat(0F, 1F).apply {
        interpolator = LinearInterpolator()
        duration = 1000
        addUpdateListener(cloudScaleAnimListener)
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
