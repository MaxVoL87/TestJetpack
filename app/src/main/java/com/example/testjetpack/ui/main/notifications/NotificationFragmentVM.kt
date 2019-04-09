package com.example.testjetpack.ui.main.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.ui.base.BaseRecyclerItemViewModel
import com.example.testjetpack.ui.base.BaseRecyclerAdapter
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.livedata.Event

class NotificationFragmentVM(
    private val repository: IDataRepository
) : BaseViewModel<NotificationFragmentVMEventStateChange>() {

    private val _notificationsResponse = MutableLiveData<LiveData<List<Notification>>>()

    val adapter = NotificationsRecyclerViewAdapter()
        .apply {
            setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<BaseRecyclerItemViewModel> {
                override fun onItemClick(position: Int, item: BaseRecyclerItemViewModel) {
                    if (item is NotificationItemViewModel)
                        _events.value =
                            Event(NotificationFragmentVMEventStateChange.OpenNotification(item.notification))
                }
            })
        }

    val adapterItems: LiveData<List<BaseRecyclerItemViewModel>> = switchMap(_notificationsResponse) { notifs ->
        MutableLiveData<List<BaseRecyclerItemViewModel>>(notifs.value?.map { NotificationItemViewModel(it) })
    }

    fun getNotifications() {
        processCallAsync(
            call = { repository.getNotifications() },
            onSuccess = { _notificationsResponse.postValue(it) },
            onError = {
                _notificationsResponse.postValue(null)
                onError(it)
            },
            showProgress = true
        )
    }
}

sealed class NotificationFragmentVMEventStateChange : EventStateChange {
    class OpenNotification(val notification: Notification) : NotificationFragmentVMEventStateChange()
}