package com.example.testjetpack.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.example.testjetpack.utils.livedata.EventObserver
import org.koin.android.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

abstract class BaseDialogFragment<B : ViewDataBinding, T : BaseViewModel<out EventStateChange>> : DialogFragment() {

    protected abstract val layoutId: Int
    protected abstract val viewModelClass: KClass<T>
    protected lateinit var binding: B
    protected val viewModel: T by lazy(LazyThreadSafetyMode.NONE) { getViewModel(viewModelClass) }

    protected open fun observeBaseLiveData() = with(viewModel) {
        events.observe(this@BaseDialogFragment, EventObserver(this@BaseDialogFragment::render))

        observeLiveData()
    }

    protected abstract val observeLiveData: T.() -> Unit

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeBaseLiveData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun render(stateChangeEvent: EventStateChange) {
        RENDERERS[stateChangeEvent::class]?.invoke(stateChangeEvent)
    }

    protected abstract val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>>
}