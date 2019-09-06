package com.example.testjetpack.ui.main.notifications

import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.ui.base.IBaseCallback

interface INotificationsFragmentCallback : IBaseCallback {

    fun openNotificationDetails(notification: Notification)
}