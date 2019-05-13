package com.example.testjetpack.ui.main.mystatus

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.testjetpack.MainApplicationContract
import com.example.testjetpack.R
import com.example.testjetpack.databinding.ItemMyStatusArcChartBinding
import com.example.testjetpack.databinding.ItemMyStatusInfoBinding
import com.example.testjetpack.ui.base.BaseRecyclerAdapter
import com.example.testjetpack.ui.base.BaseRecyclerItemViewHolder
import com.example.testjetpack.ui.base.BaseRecyclerItemViewModel
import com.example.testjetpack.utils.livedata.toMutableLiveData
import com.example.testjetpack.utils.withNotNull
import com.neo.arcchartview.ArcChartView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.reflect.KFunction1

class MyStatusViewPagerAdapter : BaseRecyclerAdapter() {

    companion object {
        const val VIEW_TYPE_ARC_CHART_ITEM = 1
        const val VIEW_TYPE_INFO_ITEM = 2
    }

    override val HOLDERS: Map<Int, Pair<Int, KFunction1<@ParameterName(name = "itemView") View, BaseRecyclerItemViewHolder<out ViewDataBinding, out BaseRecyclerItemViewModel>>>> =
        mapOf(
            VIEW_TYPE_ARC_CHART_ITEM to Pair(R.layout.item_my_status_arc_chart, ::ArcChartItemHolder),
            VIEW_TYPE_INFO_ITEM to Pair(R.layout.item_my_status_info, ::InfoItemHolder)
        )
}

class ArcChartItemHolder(itemView: View) : BaseRecyclerItemViewHolder<ItemMyStatusArcChartBinding, ArcCharItemViewModel>(itemView) {
    private val motionLayout: MotionLayout = itemView.findViewById(R.id.motionLayout)
    private val acvStatus: ArcChartView = itemView.findViewById(R.id.acvStatus)
    private val cloud: FrameLayout = itemView.findViewById(R.id.cloud)

    private val percentsObserver = Observer<Float> {
        cloudAnimator.cancel()
        cloudScaleAnimListener.setTo(it)
        cloudAnimator.start()

        setArcChartRandomValues()
    }

    override fun bindModel(binding: ItemMyStatusArcChartBinding?) {
        binding?.let { it.viewModel = viewModel }

        archSectorColors.forEach {
            val color = ContextCompat.getColor(context, it.value)
            acvStatus.setFilldeColor(it.key, color)
            acvStatus.setUnFilldeColor(it.key, ColorUtils.setAlphaComponent(color, 0x40))
        }

        sector0ArcSectorValueSetuper = ArcChartSectorValueSetuper(acvStatus, 0)
        sector1ArcSectorValueSetuper = ArcChartSectorValueSetuper(acvStatus, 1)
        sector2ArcSectorValueSetuper = ArcChartSectorValueSetuper(acvStatus, 2)
        sector3ArcSectorValueSetuper = ArcChartSectorValueSetuper(acvStatus, 3)
        sector4ArcSectorValueSetuper = ArcChartSectorValueSetuper(acvStatus, 4)
        sector5ArcSectorValueSetuper = ArcChartSectorValueSetuper(acvStatus, 5)

        withNotNull(viewModel) {
            somePercents.observe(this@ArcChartItemHolder, percentsObserver)
        }

        motionLayout.postDelayed(motionLayout::transitionToEnd, 1000)
        motionLayout.postDelayed({
            if (lifecycle.currentState == Lifecycle.State.STARTED) {
                // because of scene with multiple transitions not working properly
                // this also working bad
                motionLayout.loadLayoutDescription(R.xml.scene_my_status_arc_chart_slide)
                withNotNull(viewModel){
                    isInitialized = true
                    calculatePercents()
                }
            }
        }, 2000 + MainApplicationContract.DEFAULT_UI_DELAY) //update after first scene completed
    }

    override fun unbind() {
        super.unbind()

        sector0ArcSectorValueSetuper.cancel()
        sector1ArcSectorValueSetuper.cancel()
        sector2ArcSectorValueSetuper.cancel()
        sector3ArcSectorValueSetuper.cancel()
        sector4ArcSectorValueSetuper.cancel()
        sector5ArcSectorValueSetuper.cancel()

        cloudAnimator.cancel()
    }

    private fun setArcChartRandomValues() {
        sector0ArcSectorValueSetuper.setValue(Random.nextFloat())
        sector1ArcSectorValueSetuper.setValue(Random.nextFloat())
        sector2ArcSectorValueSetuper.setValue(Random.nextFloat())
        sector3ArcSectorValueSetuper.setValue(Random.nextFloat())
        sector4ArcSectorValueSetuper.setValue(Random.nextFloat())
        sector5ArcSectorValueSetuper.setValue(Random.nextFloat())
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

class ArcCharItemViewModel : BaseRecyclerItemViewModel() {
    override val itemViewType: Int = MyStatusViewPagerAdapter.VIEW_TYPE_ARC_CHART_ITEM

    private val _somePercents = 0.0F.toMutableLiveData()
    private val _animationProgress = 0.0F.toMutableLiveData()

    val somePercents: LiveData<Float>
        get() = _somePercents

    var maxPercents: Float = 0.8F

    val animationProgress: LiveData<Float>
        get() = _animationProgress

    var isInitialized = false

    fun calculatePercents() {

        GlobalScope.launch {
            var percents = 0
            while (percents < maxPercents * 100) {
                delay(1000)
                percents += 10
                _somePercents.postValue(percents.div(100F))
            }
        }
    }

    fun setAnimationProgress(value: Float) {
        _animationProgress.value = if (isInitialized) 1.0F - value * 2F else value
    }
}

class InfoItemHolder(itemView: View) : BaseRecyclerItemViewHolder<ItemMyStatusInfoBinding, InfoItemViewModel>(itemView) {

    override fun bindModel(binding: ItemMyStatusInfoBinding?) {
        binding?.let { it.viewModel = viewModel }
    }
}

class InfoItemViewModel : BaseRecyclerItemViewModel() {
    override val itemViewType: Int = MyStatusViewPagerAdapter.VIEW_TYPE_INFO_ITEM

    private val _animationProgress = 0.0F.toMutableLiveData()

    val animationProgress: LiveData<Float>
        get() = _animationProgress

    fun setAnimationProgress(value: Float) {
        _animationProgress.value = if(value > 0.35) value else 0.0F
    }
}