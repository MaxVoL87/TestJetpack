package com.example.testjetpack.ui.main.notifications

import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.ui.base.ICallback

interface INotificationsFragmentCallback : ICallback {

    fun openNotificationDetails(notification: Notification)
}