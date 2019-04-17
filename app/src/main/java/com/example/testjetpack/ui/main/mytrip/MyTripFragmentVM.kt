package com.example.testjetpack.ui.main.mytrip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.own.Location
import com.example.testjetpack.models.own.Trip
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import kotlinx.coroutines.*

class MyTripFragmentVM(
    private val repository: IDataRepository
) : BaseViewModel<MyTripFragmentVMEventStateChange>() {

    private val _trip = MutableLiveData<Trip>()

    val trip: LiveData<Trip>
        get() = _trip
    val position: LiveData<Location> = switchMap(_trip) {
        val posLD = MutableLiveData<Location>()
        startTrip(posLD, it)
        return@switchMap posLD
    }


    fun getTrip() {
        processCallAsync(
            call = { repository.getTrip() },
            onSuccess = { trip ->
                _trip.postValue(trip)
            },
            onError = {
                _trip.postValue(null)
                onError(it)
            },
            showProgress = true
        )
    }

    private fun startTrip(positionLD: MutableLiveData<Location>, trip: Trip) {
        GlobalScope.launch(Dispatchers.IO) {
            var prevLocation: Location? = null
            for (location in trip.locations) {
                positionLD.postValue(location)

                delay(if (prevLocation == null) 5000 else location.time - prevLocation.time) // emulate motion
                prevLocation = location
            }
        }
    }
}

sealed class MyTripFragmentVMEventStateChange : EventStateChange {
}