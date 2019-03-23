package com.example.testjetpack.ui.main.gitreposearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testjetpack.R
import com.example.testjetpack.databinding.ItemNetworkStateBinding
import com.example.testjetpack.models.git.network.NetworkState
import com.example.testjetpack.ui.base.BaseRecyclerItemViewModel

/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 */
class NetworkStateItemVH(view: View) : RecyclerView.ViewHolder(view) {
    private val binding: ItemNetworkStateBinding? = DataBindingUtil.bind(view)
    private var viewModel: NetworkStateItemVM? = null

    fun bind(networkState: NetworkState?, retryCallback: () -> Unit) {
        viewModel = if (networkState != null) NetworkStateItemVM(networkState, retryCallback) else null
        binding?.let { it.viewModel = viewModel }
    }

    companion object {
        fun create(parent: ViewGroup): NetworkStateItemVH {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_network_state, parent, false)
            return NetworkStateItemVH(view)
        }
    }
}

class NetworkStateItemVM(
    val networkState: NetworkState,
    val retryCallback: () -> Unit
) : BaseRecyclerItemViewModel() {
    override val itemViewType: Int = VIEW_TYPE_NETWORK_STATE_ITEM
}

const val VIEW_TYPE_NETWORK_STATE_ITEM = 2000