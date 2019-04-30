package com.example.testjetpack.dataflow.repository

import com.example.testjetpack.dataflow.local.AppDatabase
import com.example.testjetpack.dataflow.network.IDataApi
import com.example.testjetpack.mock.locations
import com.example.testjetpack.mock.profile
import com.example.testjetpack.models.own.Location
import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.models.own.Profile
import com.example.testjetpack.models.own.Trip

class DataRepository(
    private val dataApi: IDataApi,
    private val appDatabase: AppDatabase
) : IDataRepository {

    override fun getProfile(): Profile {
        //todo: change with api
        Thread.sleep(1500)
        return profile // init with mock data
    }

    override fun getNotifications(): List<Notification> {
        return appDatabase.getNotificationDao().loadAllByID()
    }

    override fun insertNotificationsIntoDB(notifications: List<Notification>) {
        appDatabase.getNotificationDao().insert(*notifications.toTypedArray())
    }

    override fun getTrip(): Trip {
        var tripLocations = appDatabase.getLocationDao().getLastTrip()
        if (tripLocations.isEmpty()) tripLocations = locations // init with mock data
        return Trip(tripLocations)
    }

    override fun insertLocationsIntoDB(locations: List<Location>) {
        appDatabase.getLocationDao().insert(*locations.toTypedArray())
    }

    override fun removeAllLocationsFromDB() {
        appDatabase.getLocationDao().clearAll()
    }
}