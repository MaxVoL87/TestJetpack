package com.example.testjetpack.dataflow.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.dataflow.local.AppDatabase
import com.example.testjetpack.dataflow.network.IDataApi
import com.example.testjetpack.models.gps.Location
import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.models.own.Profile
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val dataApi: IDataApi,
    private val appDatabase: AppDatabase
) : IDataRepository {

    override fun getProfile(): Profile {
        //todo: change with api
        return Profile(
            "https://picsum.photos/200/200/?random",
            "Benjamin",
            "Bankog",
            "11.07.1975",
            "MORGA753116SM9IJ",
            "MQQ346789C"
        )
    }

    override fun getNotifications(): LiveData<List<Notification>> {
        val notifications = MutableLiveData<List<Notification>>()

        appDatabase.runInTransaction {
            notifications.postValue(appDatabase.getNotificationDao().loadAllByID())
        }

        return notifications
    }

    override fun insertNotificationsIntoDB(notifications: List<Notification>) {
        appDatabase.runInTransaction {
            appDatabase.getNotificationDao().insert(*notifications.toTypedArray())
        }
    }

    override fun insertLocationsIntoDB(locations: List<Location>) {
        appDatabase.runInTransaction {
            appDatabase.getLocationDao().insert(*locations.toTypedArray())
        }
    }

    override fun removeAllLocationsfromDB() {
        appDatabase.runInTransaction {
            appDatabase.getLocationDao().clearAll()
        }
    }
}