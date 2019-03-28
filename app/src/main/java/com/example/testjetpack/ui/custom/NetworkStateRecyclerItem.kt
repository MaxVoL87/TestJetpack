package com.example.testjetpack.ui.custom

import android.view.View
import com.example.testjetpack.R
import com.example.testjetpack.databinding.ItemNetworkStateBinding
import com.example.testjetpack.models.NetworkState
import com.example.testjetpack.ui.base.BaseRecyclerItemViewHolder
import com.example.testjetpack.ui.base.BaseRecyclerItemViewModel

/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 */
class NetworkStateItemVH(view: View) : BaseRecyclerItemViewHolder<ItemNetworkStateBinding, NetworkStateItemVM>(view) {

    override fun bindModel(binding: ItemNetworkStateBinding?) {
        binding?.let { it.viewModel = viewModel }
    }

    override fun unbind() {
    }
}

class NetworkStateItemVM(
    val networkState: NetworkState,
    val retryCallback: () -> Unit
) : BaseRecyclerItemViewModel() {
    override val itemViewType: Int = VIEW_TYPE_NETWORK_STATE_ITEM
}

const val VIEW_TYPE_NETWORK_STATE_ITEM = R.layout.item_network_state