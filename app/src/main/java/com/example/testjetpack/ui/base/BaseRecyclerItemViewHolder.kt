package com.example.testjetpack.ui.base

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry


abstract class BaseRecyclerItemViewHolder<B : ViewDataBinding, M : BaseRecyclerItemViewModel>(itemView: View)
    : RecyclerView.ViewHolder(itemView), LifecycleOwner {
    protected var viewModel: M? = null
    protected var context: Context = itemView.context
    private val _lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = _lifecycleRegistry

    fun bind(viewModel: M?): B? {
        this.viewModel = viewModel
        val binding: B? = DataBindingUtil.bind(itemView)
        binding?.lifecycleOwner = this
        bindModel(binding)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        return binding
    }

    abstract fun bindModel(binding: B?)
    open fun unbind() {
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    companion object {
        inline fun <reified TYPE : BaseRecyclerItemViewHolder<ViewDataBinding, BaseRecyclerItemViewModel>> castToBaseSafe(
            item: BaseRecyclerItemViewHolder<out ViewDataBinding, out BaseRecyclerItemViewModel>?
        ) =
            if (item is TYPE) item else null
    }
}
