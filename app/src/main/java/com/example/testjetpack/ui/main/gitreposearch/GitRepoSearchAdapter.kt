package com.example.testjetpack.ui.main.gitreposearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.testjetpack.R
import com.example.testjetpack.databinding.ItemGitreposearchBinding
import com.example.testjetpack.models.GitRepositoryComplexView
import com.example.testjetpack.models.NetworkState
import com.example.testjetpack.ui.base.BaseRecyclerItemViewHolder
import com.example.testjetpack.ui.base.BaseRecyclerItemViewModel
import com.example.testjetpack.ui.base.CastExeption
import com.example.testjetpack.ui.custom.NetworkStateItemVH
import com.example.testjetpack.ui.custom.NetworkStateItemVM
import com.example.testjetpack.ui.custom.VIEW_TYPE_NETWORK_STATE_ITEM
import com.example.testjetpack.utils.LRUIndexedCache
import com.example.testjetpack.utils.withNotNull


//todo: change
class GitRepoSearchAdapter(
    private val retryCallback: () -> Unit
) : PagedListAdapter<GitRepositoryComplexView, BaseRecyclerItemViewHolder<ViewDataBinding, BaseRecyclerItemViewModel>>(
    POST_COMPARATOR
) {
    private val vmCache = LRUIndexedCache<BaseRecyclerItemViewModel>(30)

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(
        holder: BaseRecyclerItemViewHolder<ViewDataBinding, BaseRecyclerItemViewModel>,
        position: Int
    ) {
        var vm: BaseRecyclerItemViewModel? = null
        with(getItemViewType(position)) {
            if (this == VIEW_TYPE_NETWORK_STATE_ITEM) networkState?.let { vm = NetworkStateItemVM(it, retryCallback) }
            else vm = getItemViewModel(this, position)
            return@with
        }

        holder.bind(vm)
    }

    override fun onBindViewHolder(
        holder: BaseRecyclerItemViewHolder<ViewDataBinding, BaseRecyclerItemViewModel>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            //todo: change
//            val item = getItem(position)
//            (holder as GitRepoSearchItemViewHolder).updateScore(item)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerItemViewHolder<ViewDataBinding, BaseRecyclerItemViewModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        val holder = when (viewType) {
            R.layout.item_gitreposearch -> GitRepoSearchItemVH(view)
            VIEW_TYPE_NETWORK_STATE_ITEM -> NetworkStateItemVH(view)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
        return BaseRecyclerItemViewHolder.castToBaseSafe(holder)
            ?: throw CastExeption(BaseRecyclerItemViewModel::class.java)
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            VIEW_TYPE_NETWORK_STATE_ITEM
        } else {
            R.layout.item_gitreposearch
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    /**
     * create VM or get from cache
     */
    private fun getItemViewModel(itemViewType: Int, position: Int): BaseRecyclerItemViewModel? {
        var vm: BaseRecyclerItemViewModel? = null

        val item = getItem(position)
        var vmCached = vmCache[position]

        when (itemViewType) {
            R.layout.item_gitreposearch -> {
                if (vmCached is GitRepoSearchItemVM && vmCached.repo == item) {
                    vm = vmCached
                } else {
                    vmCached = null
                    withNotNull(item) {
                        vm = GitRepoSearchItemVM(this)
                    }
                }
            }
            else -> NotImplementedError("Unknown ItemViewType")
        }

        withNotNull(vm) {
            if (vmCached == null) vmCache.put(position, this)
        }

        return vm
    }

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<GitRepositoryComplexView>() {
            override fun areContentsTheSame(oldItem: GitRepositoryComplexView, newItem: GitRepositoryComplexView): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: GitRepositoryComplexView, newItem: GitRepositoryComplexView): Boolean =
                oldItem.index == newItem.index

        }
    }
}

class GitRepoSearchItemVH(view: View) :
    BaseRecyclerItemViewHolder<ItemGitreposearchBinding, GitRepoSearchItemVM>(view) {

    override fun bindModel(binding: ItemGitreposearchBinding?) {
        binding?.let { it.viewModel = viewModel }
    }

    override fun unbind() {
    }
}

class GitRepoSearchItemVM(
    val repo: GitRepositoryComplexView
) : BaseRecyclerItemViewModel() {
    override val itemViewType: Int = -1 //temp todo: change
}