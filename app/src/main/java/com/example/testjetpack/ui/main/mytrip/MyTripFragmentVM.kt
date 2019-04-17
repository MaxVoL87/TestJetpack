package com.example.testjetpack.ui.main.mytrip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.own.Trip
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*

class MyTripFragmentVM(
    private val repository: IDataRepository
) : BaseViewModel<MyTripFragmentVMEventStateChange>() {

    private val _trip = MutableLiveData<Trip>()

    val trip: LiveData<Trip>
        get() = _trip
    val position: LiveData<LatLng> = switchMap(_trip) {
        val posLD = MutableLiveData<LatLng>()
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

    private fun startTrip(positionLD: MutableLiveData<LatLng>, trip: Trip) {
        GlobalScope.launch(Dispatchers.IO) {
            for (location in trip.locations) {
                positionLD.postValue(location)
                delay(5000)
            }
        }
    }
}

sealed class MyTripFragmentVMEventStateChange : EventStateChange {
}