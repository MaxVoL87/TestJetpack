package com.example.testjetpack.ui.main.gps

import android.annotation.SuppressLint
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.livedata.Event
import com.example.testjetpack.utils.toDBEntity
import com.google.android.gms.location.*
import java.util.*
import javax.inject.Inject

class GpsFragmentVM : BaseViewModel<GpsFragmentVMEventStateChange>() {
    private val INTERVAL: Long = 1500
    private val FASTEST_INTERVAL: Long = 1000
    private var _startTime: Long? = null
    private var _fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val _locationRequest = LocationRequest().apply {
        // Create the location request to start receiving updates
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = INTERVAL
        fastestInterval = FASTEST_INTERVAL
    }
    private val _locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val startTime = _startTime ?: return

            // do work here
            processCallAsync(
                call = { dataRepository.insertLocationsIntoDB(listOf(locationResult.lastLocation.toDBEntity(startTime))) },
                onSuccess = {},
                onError = {
                    onError(it)
                },
                showProgress = true
            )

        }
    }

    @Inject
    lateinit var dataRepository: IDataRepository

    init {
        MainApplication.component.inject(this)
    }

    private val _isLocationListenerStarted = MutableLiveData<Boolean>(false)

    val isLocationListenerStarted: LiveData<Boolean>
        get() = _isLocationListenerStarted


    fun startStopLocationUpdates(
        settingsClient: SettingsClient,
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        if (_isLocationListenerStarted.value == true) {
            stopLocationUpdates()
            return
        }

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(_locationRequest)
        val locationSettingsRequest = builder.build()

        settingsClient.checkLocationSettings(locationSettingsRequest)
        _fusedLocationProviderClient = fusedLocationProviderClient

        _events.value = Event(GpsFragmentVMEventStateChange.RequestLocationUpdatesPermissions)
    }

    @SuppressLint("MissingPermission")
    fun onPermissionSuccess() {
        _startTime = Calendar.getInstance().time.time
        _fusedLocationProviderClient?.requestLocationUpdates(_locationRequest, _locationCallback, Looper.myLooper())
        _isLocationListenerStarted.value = true
    }

    private fun stopLocationUpdates() {
        _fusedLocationProviderClient?.removeLocationUpdates(_locationCallback)
        _fusedLocationProviderClient = null
        _isLocationListenerStarted.value = false
    }

}

sealed class GpsFragmentVMEventStateChange : EventStateChange {
    object RequestLocationUpdatesPermissions : GpsFragmentVMEventStateChange()
}