package com.example.testjetpack.ui.main.mystatus


import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.Observer

import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentMyStatusBinding
import com.example.testjetpack.ui.base.BaseFragmentWithCallback
import com.example.testjetpack.ui.base.EventStateChange
import com.neo.arcchartview.ArcChartView
import kotlinx.android.synthetic.main.fragment_my_status.*
import kotlin.random.Random
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
            somePercents.observe(this@MyStatusFragment, Observer {
                cloudAnimator.cancel()
                cloudScaleAnimListener.setTo(it)
                cloudAnimator.start()

                setArcChartRandomValues()
            })
        }


    private val cloudScaleAnimListener = object : ValueAnimator.AnimatorUpdateListener {
        private var cloudScale = 0.0F
        private var cloudScaleTo = 0.0F

        fun setTo(to: Float) {
            if (to > 1F) throw IllegalArgumentException("to > 1F")
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

    private lateinit var sector0ArcSectorValueSetuper: ArcChartSectorValueSetuper
    private lateinit var sector1ArcSectorValueSetuper: ArcChartSectorValueSetuper
    private lateinit var sector2ArcSectorValueSetuper: ArcChartSectorValueSetuper
    private lateinit var sector3ArcSectorValueSetuper: ArcChartSectorValueSetuper
    private lateinit var sector4ArcSectorValueSetuper: ArcChartSectorValueSetuper
    private lateinit var sector5ArcSectorValueSetuper: ArcChartSectorValueSetuper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        archSectorColors.forEach {
            val color = ContextCompat.getColor(requireContext(), it.value)
            acvStatus.setFilldeColor(it.key, color)
            acvStatus.setUnFilldeColor(it.key, ColorUtils.setAlphaComponent(color, 0x40))
        }

        sector0ArcSectorValueSetuper = ArcChartSectorValueSetuper(acvStatus, 0)
        sector1ArcSectorValueSetuper = ArcChartSectorValueSetuper(acvStatus, 1)
        sector2ArcSectorValueSetuper = ArcChartSectorValueSetuper(acvStatus, 2)
        sector3ArcSectorValueSetuper = ArcChartSectorValueSetuper(acvStatus, 3)
        sector4ArcSectorValueSetuper = ArcChartSectorValueSetuper(acvStatus, 4)
        sector5ArcSectorValueSetuper = ArcChartSectorValueSetuper(acvStatus, 5)

        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                // because of scene with multiple transitions not working properly
                // this also working bad
                motionLayout.loadLayoutDescription(R.xml.scene_my_status_slide)
            }

        })
    }

    private fun setArcChartRandomValues() {
        sector0ArcSectorValueSetuper.setValue(Random.nextFloat())
        sector1ArcSectorValueSetuper.setValue(Random.nextFloat())
        sector2ArcSectorValueSetuper.setValue(Random.nextFloat())
        sector3ArcSectorValueSetuper.setValue(Random.nextFloat())
        sector4ArcSectorValueSetuper.setValue(Random.nextFloat())
        sector5ArcSectorValueSetuper.setValue(Random.nextFloat())
    }

    override fun onStart() {
        super.onStart()
        motionLayout.postDelayed(motionLayout::transitionToEnd, 1000)
        //motionLayout.layoutTransition = LayoutTransition(motionLayout.definedTransitions[1]).se
    }

    override fun onStop() {
        sector0ArcSectorValueSetuper.cancel()
        sector1ArcSectorValueSetuper.cancel()
        sector2ArcSectorValueSetuper.cancel()
        sector3ArcSectorValueSetuper.cancel()
        sector4ArcSectorValueSetuper.cancel()
        sector5ArcSectorValueSetuper.cancel()

        cloudAnimator.cancel()

        super.onStop()
    }

    // region VM events renderer

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
    )

    // endregion VM events renderer

    private val archSectorColors: Map<Int, Int> = mapOf(
        0 to R.color.arch_chart_sector_0,
        1 to R.color.arch_chart_sector_1,
        2 to R.color.arch_chart_sector_2,
        3 to R.color.arch_chart_sector_3,
        4 to R.color.arch_chart_sector_4,
        5 to R.color.arch_chart_sector_5
    )

    private class ArcChartSectorValueSetuper(private val view: ArcChartView, private val sector: Int) {
        private val arcChartScaleAnimListener = object : ValueAnimator.AnimatorUpdateListener {
            private var sectorFrom = 0.0F
            private var sectorTo = 0.0F

            fun setTo(to: Float) {
                if (to > 1F) throw IllegalArgumentException("to > 1F")
                sectorFrom = sectorTo
                sectorTo = to
            }

            override fun onAnimationUpdate(it: ValueAnimator) {
                val value = it.animatedFraction
                sectorFrom = sectorFrom * (1 - value) + sectorTo * value
                view.setSectionValue(sector, (sectorFrom * 100).toInt())
            }
        }
        private val arcChartAnimator: ValueAnimator = ValueAnimator.ofFloat(0F, 1F).apply {
            interpolator = LinearInterpolator()
            duration = 1000
            addUpdateListener(arcChartScaleAnimListener)
        }

        fun setValue(value: Float) {
            arcChartAnimator.cancel()
            arcChartScaleAnimListener.setTo(value)
            arcChartAnimator.start()
        }

        fun cancel() {
            arcChartAnimator.cancel()
        }
    }
}
