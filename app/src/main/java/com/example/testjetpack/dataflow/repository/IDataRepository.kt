package com.example.testjetpack.dataflow.repository

import androidx.lifecycle.LiveData
import com.example.testjetpack.models.own.Location
import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.models.own.Profile

interface IDataRepository {

    fun getProfile(): Profile

    fun getNotifications(): LiveData<List<Notification>>

    fun insertNotificationsIntoDB(notifications: List<Notification>)


    fun insertLocationsIntoDB(locations: List<Location>)

    fun removeAllLocationsfromDB()
}