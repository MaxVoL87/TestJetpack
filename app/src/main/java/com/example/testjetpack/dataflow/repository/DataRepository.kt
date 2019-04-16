package com.example.testjetpack.dataflow.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.dataflow.local.AppDatabase
import com.example.testjetpack.dataflow.network.IDataApi
import com.example.testjetpack.models.own.Location
import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.models.own.Profile
import com.example.testjetpack.models.own.Trip
import com.example.testjetpack.utils.fromJson
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

class DataRepository(
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

    override fun getTrip(): Trip{
        val mockTrip = "[" +
                "{\"latitude\":39.877812,\"longitude\":-86.122479}," +
                "{\"latitude\":39.891391,\"longitude\":-86.121339}," +
                "{\"latitude\":39.904824,\"longitude\":-86.121168}," +
                "{\"latitude\":39.910091,\"longitude\":-86.116533}," +
                "{\"latitude\":39.911671,\"longitude\":-86.106748}," +
                "{\"latitude\":39.908248,\"longitude\":-86.09559}," +
                "{\"latitude\":39.905877,\"longitude\":-86.081686}," +
                "{\"latitude\":39.905351,\"longitude\":-86.069841}," +
                "{\"latitude\":39.905351,\"longitude\":-86.055593}," +
                "{\"latitude\":39.905087,\"longitude\":-86.044778}," +
                "{\"latitude\":39.896659,\"longitude\":-86.045293}," +
                "{\"latitude\":39.891391,\"longitude\":-86.046152}," +
                "{\"latitude\":39.885859,\"longitude\":-86.045637}," +
                "{\"latitude\":39.880326,\"longitude\":-86.044778}," +
                "{\"latitude\":39.87453,\"longitude\":-86.04495}," +
                "{\"latitude\":39.86926,\"longitude\":-86.045808}," +
                "{\"latitude\":39.863463,\"longitude\":-86.045293}," +
                "{\"latitude\":39.85582,\"longitude\":-86.046152}," +
                "{\"latitude\":39.854239,\"longitude\":-86.05731}," +
                "{\"latitude\":39.855557,\"longitude\":-86.070528}" +
                "]"
        return Trip(Gson().fromJson<List<LatLng>>(mockTrip)!!)
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