package com.example.testjetpack.dataflow.repository

import com.example.testjetpack.dataflow.local.AppDatabase
import com.example.testjetpack.dataflow.local.ILocationDao
import com.example.testjetpack.dataflow.local.INotificationDao
import com.example.testjetpack.dataflow.network.IDataApi
import com.example.testjetpack.mock.locations
import com.example.testjetpack.mock.profile
import com.example.testjetpack.models.own.Location
import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.models.own.Profile
import com.example.testjetpack.models.own.Trip

class DataRepository(
    private val _dataApi: IDataApi,
    private val _appDatabase: AppDatabase
) : IDataRepository {

    private val _notificationDao: INotificationDao by lazy { _appDatabase.getNotificationDao() }
    private val _locationDao: ILocationDao by lazy { _appDatabase.getLocationDao() }


    override fun getProfile(): Profile {
        //todo: change with api
        Thread.sleep(1500)
        return profile // init with mock data
    }

    override fun insertNotificationsIntoDB(notifications: List<Notification>) {
        _notificationDao.insert(*notifications.toTypedArray())
    }

    override fun removeAllNotificationsFromDB() {
        _notificationDao.clearAll()
    }

    override fun getNotifications(): List<Notification> {
        return _notificationDao.loadAllByID()
    }


    override fun insertLocationsIntoDB(locations: List<Location>) {
        _locationDao.insert(*locations.toTypedArray())
    }

    override fun removeAllLocationsFromDB() {
        _locationDao.clearAll()
    }

    override fun getTrip(): Trip {
        var tripLocations = _locationDao.getLastTrip()
        if (tripLocations.isEmpty()) tripLocations = locations // init with mock data
        return Trip(tripLocations)
    }
}