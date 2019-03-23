package com.example.testjetpack.ui.base

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.ViewDataBinding


abstract class BaseRecyclerItemViewHolder<B: ViewDataBinding, M : BaseRecyclerItemViewModel>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var viewModel: M? = null

    fun bind(viewModel: M?): B? {
        this.viewModel = viewModel
        val binding: B? = DataBindingUtil.bind(itemView)
        bindModel(binding)
        return binding
    }

    abstract fun bindModel(binding: B?)
    abstract fun unbind()

    companion object {
        inline fun <reified TYPE : BaseRecyclerItemViewHolder<ViewDataBinding, BaseRecyclerItemViewModel>> castToBaseSafe(item: BaseRecyclerItemViewHolder<out ViewDataBinding, out BaseRecyclerItemViewModel>?) =
            if (item is TYPE) item else null
    }
}
