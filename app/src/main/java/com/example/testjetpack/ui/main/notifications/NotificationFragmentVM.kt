package com.example.testjetpack.ui.main.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    val adapter: BaseRecyclerAdapter = NotificationsRecyclerViewAdapter()
    val onItemClickListener: BaseRecyclerAdapter.OnItemClickListener<BaseRecyclerItemViewModel> =
        object : BaseRecyclerAdapter.OnItemClickListener<BaseRecyclerItemViewModel> {
            override fun onItemClick(position: Int, item: BaseRecyclerItemViewModel) {
                if (item is NotificationItemViewModel)
                    _events.value = Event(NotificationFragmentVMEventStateChange.OpenNotification(item.notification))
            }
        }
    val itemViewModels: LiveData<List<BaseRecyclerItemViewModel>>
        get() = _itemViewModels

    private val _itemViewModels: MutableLiveData<List<BaseRecyclerItemViewModel>> = MutableLiveData()

    fun getNotifications() {
        processAsyncCall(
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
        if (notifications.isNullOrEmpty()) {
            _itemViewModels.postValue(mutableListOf())
            return
        }
        _itemViewModels.postValue(notifications.map { NotificationItemViewModel(it) })
    }
}

sealed class NotificationFragmentVMEventStateChange : EventStateChange {
    class OpenNotification(val notification: Notification) : NotificationFragmentVMEventStateChange()
}