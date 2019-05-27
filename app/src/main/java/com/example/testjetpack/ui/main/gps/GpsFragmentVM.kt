package com.example.testjetpack.ui.main.gps

import android.annotation.SuppressLint
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import com.example.testjetpack.dataflow.gps.GpsLocationProvider
import com.example.testjetpack.dataflow.gps.ILocationProviderListener
import com.example.testjetpack.dataflow.gps.GmsLocationProvider
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.own.Location
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.ui.base.UnexpectedExeption
import com.example.testjetpack.utils.livedata.Event
import com.example.testjetpack.utils.livedata.toLDString
import com.example.testjetpack.utils.livedata.toMutableLiveData
import com.example.testjetpack.utils.toDBEntity
import java.util.*
import java.util.concurrent.TimeUnit

class GpsFragmentVM(
    private val dataRepository: IDataRepository,
    private val gmsLocationProvider: GmsLocationProvider,
    private val gpsLocationProvider: GpsLocationProvider
) : BaseViewModel<GpsFragmentVMEventStateChange>() {

    //initial values
    private val _interval: Long = 1000
    private var _startTime: Long? = null

    private val _locationCallback = object : ILocationProviderListener {
        override fun onLocationCalculated(location: android.location.Location) {
            val startTime = _startTime ?: throw UnexpectedExeption("onLocationResult.startTime == null")
            //if (_isLocationAvailable.value != true) return if not available - no location

            val dbLocation = location.toDBEntity(startTime)
            _curLocation = dbLocation

            processCallAsync(
                call = { dataRepository.insertLocationsIntoDB(listOf(dbLocation)) },
                onSuccess = {},
                onError = {
                    onError(it)
                },
                showProgress = true
            )
        }

        override fun onLocationAvailable(isAvailable: Boolean) {
            _isLocationAvailable.value = isAvailable
        }

    }

    private var _curLocation: Location? = null
        set(value) {
            field = value
            initLocationValues(value)
        }

    private val _location = MutableLiveData<Location>()
    private val _isLocationAvailable = false.toMutableLiveData()
    private val _isLocationListenerStarted = false.toMutableLiveData()

    val isLocationAvailable: LiveData<Boolean>
        get() = _isLocationAvailable

    val isLocationListenerStarted: LiveData<Boolean>
        get() = _isLocationListenerStarted

    val isNeedToShowDiagnostic = true.toMutableLiveData()
    val isGPSOnly = false.toMutableLiveData()

    val latitude: LiveData<String> = switchMap(_location) { it.latitude.toLDString() }
    val longitude: LiveData<String> = switchMap(_location) { it.longitude.toLDString() }
    val altitude: LiveData<String> = switchMap(_location) { it.altitude.toLDString() }
    val bearing: LiveData<String> = switchMap(_location) { it.bearing.toLDString() }
    val speed: LiveData<String> = switchMap(_location) { it.speed.toLDString() }
    val accuracy: LiveData<String> = switchMap(_location) { it.accuracy.toLDString() }
    val verticalAccuracy: LiveData<String> = switchMap(_location) { it.verticalAccuracyMeters.toLDString() }
    val speedAccuracy: LiveData<String> = switchMap(_location) { it.speedAccuracyMetersPerSecond.toLDString() }
    val bearingAccuracy: LiveData<String> = switchMap(_location) { it.bearingAccuracyDegrees.toLDString() }
    val time: LiveData<String> = switchMap(_location) { it.time.toLDString() }
    val elapsedRealTime: LiveData<String> =
        switchMap(_location) { TimeUnit.NANOSECONDS.toMillis(it.elapsedRealtimeNanos).toLDString() }

    val acceleration: LiveData<String> = switchMap(_location) { it.acceleration.toLDString() }
    val satellites: LiveData<String> = switchMap(_location) { it.satellites.toLDString() }

    fun startStopLocationUpdates() {
        if (_isLocationListenerStarted.value == true) {
            stopLocationUpdates()
            return
        }

        _events.value = Event(GpsFragmentVMEventStateChange.RequestLocationUpdatesPermissions)
    }

    @SuppressLint("MissingPermission")
    fun onPermissionSuccess() {
        try {
            if (isGPSOnly.value == true) {
                gpsLocationProvider
                    .setInterval(_interval)
                    .requestLocationUpdates(_locationCallback)
            } else {
                gmsLocationProvider
                    .setInterval(_interval)
                    .setPriority(GmsLocationProvider.Priority.HIGH)
                    .requestLocationUpdates(_locationCallback, Looper.getMainLooper())
            }
        } catch (ex: Throwable) {
            showAlert(ex)
            return
        }

        _startTime = Calendar.getInstance().time.time
        _isLocationListenerStarted.value = true
    }

    fun clearDBData(onSuccess: () -> Unit) {
        processCallAsync(
            call = { dataRepository.removeAllLocationsFromDB() },
            onSuccess = { onSuccess },
            onError = {
                onError(it)
            },
            showProgress = true
        )
    }

    private fun stopLocationUpdates() {
        if (gmsLocationProvider.isAlive) gmsLocationProvider.stopLocationUpdates()
        if (gpsLocationProvider.isAlive) gpsLocationProvider.stopLocationUpdates()
        _isLocationListenerStarted.value = false
    }

    private fun initLocationValues(location: Location?) {
        if (isNeedToShowDiagnostic.value != true) return
        if (location == null) {
            _isLocationAvailable.value = false
            return
        }
        _location.value = location
    }
}

sealed class GpsFragmentVMEventStateChange : EventStateChange {
    object RequestLocationUpdatesPermissions : GpsFragmentVMEventStateChange()
}