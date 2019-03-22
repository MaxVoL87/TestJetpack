package com.example.testjetpack.ui.main.notifications

import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.Notification
import com.example.testjetpack.ui.base.BaseRecyclerItemViewModel
import com.example.testjetpack.ui.base.BaseRecyclerAdapter
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.livedata.Event
import javax.inject.Inject

class NotificationFragmentVM : BaseViewModel<NotificationFragmentVMEventStateChange>() {

    @Inject
    lateinit var repository: IDataRepository

    init {
        MainApplication.component.inject(this)
    }

    val adapter = NotificationsRecyclerViewAdapter()
        .apply {
            setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<BaseRecyclerItemViewModel> {
                override fun onItemClick(position: Int, item: BaseRecyclerItemViewModel) {
                    if (item is NotificationItemViewModel)
                        _events.value = Event(NotificationFragmentVMEventStateChange.OpenNotification(item.notification))
                }
            })
        }

    fun getNotifications() {
        processCallAsync(
            call = { repository.getNotificationsAsync() },
            onSuccess = { notifications ->
                setItemsList(notifications)
            },
            onError = {
                setItemsList(null)
                onError(it)
            },
            showProgress = true
        )
    }

    private fun setItemsList(notifications: List<Notification>?) {
        adapter.itemViewModels =
            if (notifications.isNullOrEmpty()) mutableListOf()
            else notifications.map { NotificationItemViewModel(it) }.toMutableList()
    }
}

sealed class NotificationFragmentVMEventStateChange : EventStateChange {
    class OpenNotification(val notification: Notification) : NotificationFragmentVMEventStateChange()
}