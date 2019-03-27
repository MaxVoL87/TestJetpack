package com.example.testjetpack.ui.main.notifications

import com.example.testjetpack.models.own.Notification

interface INotificationFragmentCallback {

    fun openNotificationDetails(notification: Notification)
}