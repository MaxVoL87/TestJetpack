package com.example.testjetpack.utils.binding

import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.example.testjetpack.models.NetworkState
import com.example.testjetpack.models.git.db.GitRepositoryView
import com.example.testjetpack.ui.main.gitreposearch.GitRepoSearchAdapter
import com.example.testjetpack.utils.withNotNull


/**
 * Set PagedListAdapter data
 */
@BindingAdapter("items")
fun RecyclerView.setPagedListAdapterData(
    items: PagedList<GitRepositoryView>?
) {
    withNotNull(adapter as GitRepoSearchAdapter) {
        submitList(items)
    }
}

/**
 * Set PagedListAdapter networkState
 */
@BindingAdapter("networkState")
fun RecyclerView.setPagedListAdapterNetworkState(
    networkState: NetworkState?
) {
    withNotNull(adapter as GitRepoSearchAdapter) {
        setNetworkState(networkState)
    }
}