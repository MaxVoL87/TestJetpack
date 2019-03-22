package com.example.testjetpack.ui.main.gitreposearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testjetpack.R
import com.example.testjetpack.databinding.ItemGitreposearchBinding
import com.example.testjetpack.models.git.GitRepository
import com.example.testjetpack.ui.base.BaseRecyclerItemViewModel

class GitRepoSearchItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding: ItemGitreposearchBinding? = DataBindingUtil.bind(view)
    private var viewModel: GitRepoSearchItemViewModel? = null

    fun bind(repo: GitRepository?) {
        if(repo == null) return // todo: change to empty
        viewModel = GitRepoSearchItemViewModel(repo)
        binding?.let { it.viewModel = viewModel }
    }

    companion object {
        fun create(parent: ViewGroup): GitRepoSearchItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gitreposearch, parent, false)
            return GitRepoSearchItemViewHolder(view)
        }
    }
}

class GitRepoSearchItemViewModel(
    val repo: GitRepository
) : BaseRecyclerItemViewModel() {
    override val itemViewType: Int = -1 //temp todo: change
}