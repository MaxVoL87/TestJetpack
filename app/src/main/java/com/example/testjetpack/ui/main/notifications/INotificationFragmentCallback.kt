package com.example.testjetpack.ui.main.notifications

import com.example.testjetpack.models.Notification

interface INotificationFragmentCallback {

    fun openNotificationDetails(notification: Notification)
}