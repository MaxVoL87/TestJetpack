package com.example.testjetpack.ui.main.notifications

import com.example.testjetpack.models.own.Notification

interface INotificationsFragmentCallback {

    fun openNotificationDetails(notification: Notification)
}