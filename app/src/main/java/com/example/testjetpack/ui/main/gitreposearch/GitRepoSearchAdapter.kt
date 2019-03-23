package com.example.testjetpack.ui.main.gitreposearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testjetpack.R
import com.example.testjetpack.databinding.ItemGitreposearchBinding
import com.example.testjetpack.models.git.GitRepository
import com.example.testjetpack.models.git.network.NetworkState
import com.example.testjetpack.ui.base.BaseRecyclerItemViewModel


//todo: change
class GitRepoSearchAdapter  (private val retryCallback: () -> Unit)
: PagedListAdapter<GitRepository, RecyclerView.ViewHolder>(POST_COMPARATOR) {

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_gitreposearch -> (holder as GitRepoSearchItemVH).bind(getItem(position))
            R.layout.item_network_state -> (holder as NetworkStateItemVH).bind(networkState, retryCallback)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            //todo: change
//            val item = getItem(position)
//            (holder as GitRepoSearchItemViewHolder).updateScore(item)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_gitreposearch -> GitRepoSearchItemVH.create(parent)
            R.layout.item_network_state -> NetworkStateItemVH.create(parent)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
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

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<GitRepository>() {
            override fun areContentsTheSame(oldItem: GitRepository, newItem: GitRepository): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: GitRepository, newItem: GitRepository): Boolean =
                oldItem.name == newItem.name

        }
    }
}

class GitRepoSearchItemVH(view: View) : RecyclerView.ViewHolder(view) {
    private val binding: ItemGitreposearchBinding? = DataBindingUtil.bind(view)
    private var viewModel: GitRepoSearchItemVM? = null

    fun bind(repo: GitRepository?) {
        viewModel = if (repo != null) GitRepoSearchItemVM(repo) else null
        binding?.let { it.viewModel = viewModel }
    }

    companion object {
        fun create(parent: ViewGroup): GitRepoSearchItemVH {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gitreposearch, parent, false)
            return GitRepoSearchItemVH(view)
        }
    }
}

class GitRepoSearchItemVM(
    val repo: GitRepository
) : BaseRecyclerItemViewModel() {
    override val itemViewType: Int = -1 //temp todo: change
}