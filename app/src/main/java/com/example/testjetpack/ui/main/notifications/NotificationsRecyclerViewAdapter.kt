package com.example.testjetpack.ui.main.notifications

import android.view.View
import androidx.databinding.ViewDataBinding
import com.example.testjetpack.R
import com.example.testjetpack.databinding.ItemNotificationBinding
import com.example.testjetpack.models.Notification
import com.example.testjetpack.ui.base.BaseRecyclerItemViewModel
import com.example.testjetpack.ui.base.BaseRecyclerAdapter
import com.example.testjetpack.ui.base.BaseRecyclerItemViewHolder
import com.example.testjetpack.ui.main.notifications.NotificationsRecyclerViewAdapter.Companion.VIEW_TYPE_NOTIFICATION_ITEM

import kotlin.reflect.KFunction1

class NotificationsRecyclerViewAdapter : BaseRecyclerAdapter() {

   companion object {
        const val VIEW_TYPE_NOTIFICATION_ITEM = 1
    }

    override val HOLDERS: Map<Int, Pair<Int, KFunction1<@ParameterName(name = "itemView") View,  BaseRecyclerItemViewHolder<out ViewDataBinding, out BaseRecyclerItemViewModel>>>> = mapOf(
        VIEW_TYPE_NOTIFICATION_ITEM to Pair(R.layout.item_notification, ::NotificationItemHolder)
    )

}

class NotificationItemHolder(itemView: View) : BaseRecyclerItemViewHolder<ItemNotificationBinding, NotificationItemViewModel>(itemView) {
    override lateinit var viewModel: NotificationItemViewModel

    override fun bindModel(binding: ItemNotificationBinding?) {
        binding?.let { it.notificationItemViewModel = viewModel }
    }

    override fun unbind() {
    }

}

class NotificationItemViewModel(
    val notification: Notification
) : BaseRecyclerItemViewModel() {
    override val itemViewType: Int = VIEW_TYPE_NOTIFICATION_ITEM
}
