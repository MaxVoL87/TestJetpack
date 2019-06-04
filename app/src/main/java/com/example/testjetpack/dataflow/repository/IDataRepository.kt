package com.example.testjetpack.dataflow.repository

import com.example.testjetpack.models.own.Location
import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.models.own.Profile
import com.example.testjetpack.models.own.Trip

interface IDataRepository {

    fun getProfile(): Profile


    fun insertNotificationsIntoDB(notifications: List<Notification>)

    fun removeAllNotificationsFromDB()

    fun getNotifications(): List<Notification>


    fun insertLocationsIntoDB(locations: List<Location>)

    fun removeAllLocationsFromDB()

    fun getTrip(): Trip
}