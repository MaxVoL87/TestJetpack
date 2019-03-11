package com.example.testjetpack.utils.binding

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.testjetpack.ui.base.BaseRecyclerItemViewModel
import com.example.testjetpack.ui.base.BaseRecyclerAdapter

@BindingAdapter("bindingAdapter", "bindingItemViewModels", "bindingAdapterClickListener", requireAll = false)
fun RecyclerView.setBindingAdapterWithListener(
    bindingAdapter: BaseRecyclerAdapter?,
    bindingItemViewModels: LiveData<List<BaseRecyclerItemViewModel>>?,
    bindingAdapterClickListener: BaseRecyclerAdapter.OnItemClickListener<BaseRecyclerItemViewModel>?
) {
    var mAdapter = adapter
    bindingAdapter?.let { mAdapter = it }

    val tmAdapter = mAdapter
    if (tmAdapter is BaseRecyclerAdapter) {
        bindingItemViewModels?.value?.let { tmAdapter.itemViewModels = it.toMutableList() }
        bindingAdapterClickListener?.let { tmAdapter.setOnItemClickListener(it) }
    }
    adapter = tmAdapter
}