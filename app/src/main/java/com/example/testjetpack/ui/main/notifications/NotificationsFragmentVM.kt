package com.example.testjetpack.ui.main.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.mock.notifications
import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.ui.base.BaseRecyclerItemViewModel
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.livedata.Event
import java.util.*

class NotificationsFragmentVM(
    private val _repository: IDataRepository
) : BaseViewModel<NotificationsFragmentVMEventStateChange>() {

    private val _notificationsResponse = MutableLiveData<List<Notification>>()

    val adapter = NotificationsRecyclerViewAdapter()

    val adapterItems: LiveData<List<BaseRecyclerItemViewModel>> = switchMap(_notificationsResponse) { notifs ->
        MutableLiveData<List<BaseRecyclerItemViewModel>>(notifs.map {
            NotificationItemViewModel(it) { notification ->
                _events.value = Event(NotificationsFragmentVMEventStateChange.OpenNotifications(notification))
            }
        })
    }

    fun getNotifications() {
        processCallAsync(
            call = { _repository.getNotifications() },
            onSuccess = { _notificationsResponse.postValue(it) },
            onError = {
                _notificationsResponse.postValue(null)
                onError(it)
            },
            showProgress = true
        )
    }

    fun addNotifications() {
        processCallAsync(
            call = {
                var curTime = Calendar.getInstance().time.time
                _repository.insertNotificationsIntoDB(notifications.map { it.copy(id = curTime++.toString()) })
            },
            onSuccess = {
                getNotifications()
                _events.value = Event(NotificationsFragmentVMEventStateChange.OnNotificationsAdded)
            },
            onError = {
                onError(it)
            },
            showProgress = true
        )
    }

    fun clearNotifications() {
        processCallAsync(
            call = { _repository.removeAllNotificationsFromDB() },
            onSuccess = {
                getNotifications()
                _events.value = Event(NotificationsFragmentVMEventStateChange.OnNotificationsCleared)
            },
            onError = {
                onError(it)
            },
            showProgress = true
        )
    }
}

sealed class NotificationsFragmentVMEventStateChange : EventStateChange {
    class OpenNotifications(val notification: Notification) : NotificationsFragmentVMEventStateChange()
    object OnNotificationsAdded : NotificationsFragmentVMEventStateChange()
    object OnNotificationsCleared : NotificationsFragmentVMEventStateChange()
}