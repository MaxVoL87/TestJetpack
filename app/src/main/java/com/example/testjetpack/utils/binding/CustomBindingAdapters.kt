package com.example.testjetpack.utils.binding

import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.example.testjetpack.models.git.GitRepository
import com.example.testjetpack.models.git.network.NetworkState
import com.example.testjetpack.ui.main.gitreposearch.GitRepoSearchAdapter
import com.example.testjetpack.utils.withNotNull


/**
 * Set PagedListAdapter data
 */
@BindingAdapter("items", "networkState", requireAll = false)
fun RecyclerView.setPagedListAdapterData(
    items: PagedList<GitRepository>?,
    networkState: NetworkState?
) {
    withNotNull(adapter as GitRepoSearchAdapter){
        items?.let { submitList(it) }
        networkState?.let { setNetworkState(it) }
        null
    }
}