package com.example.testjetpack.ui.main.gps

import android.annotation.SuppressLint
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.gps.ILocationProviderListener
import com.example.testjetpack.dataflow.gps.LocationProvider
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.gps.Location
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.ui.base.UnexpectedExeption
import com.example.testjetpack.utils.livedata.Event
import com.example.testjetpack.utils.toDBEntity
import java.util.*
import javax.inject.Inject

class GpsFragmentVM : BaseViewModel<GpsFragmentVMEventStateChange>() {

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

    @Inject
    lateinit var dataRepository: IDataRepository
    @Inject
    lateinit var locationProvider: LocationProvider

    init {
        MainApplication.component.inject(this)
    }

    private var _curLocation: Location? = null
        set(value) {
            field = value
            if (isNeedToShowDiagnostic.value == true) _location.value = value
        }

    private val _location = MutableLiveData<Location>()
    private val _isLocationAvailable = MutableLiveData<Boolean>(false)
    private val _isLocationListenerStarted = MutableLiveData<Boolean>(false)

    val isLocationAvailable: LiveData<Boolean>
        get() = _isLocationAvailable

    val isLocationListenerStarted: LiveData<Boolean>
        get() = _isLocationListenerStarted

    val isNeedToShowDiagnostic = MutableLiveData<Boolean>(true)

    val latitude: LiveData<String> = switchMap(_location) { MutableLiveData(it.latitude.toString()) }
    val longitude: LiveData<String> = switchMap(_location) { MutableLiveData(it.longitude.toString()) }
    val altitude: LiveData<String> = switchMap(_location) { MutableLiveData(it.altitude.toString()) }
    val bearing: LiveData<String> = switchMap(_location) { MutableLiveData(it.bearing.toString()) }
    val speed: LiveData<String> = switchMap(_location) { MutableLiveData(it.speed.toString()) }
    val accuracy: LiveData<String> = switchMap(_location) { MutableLiveData(it.accuracy.toString()) }
    val verticalAccuracy: LiveData<String> = switchMap(_location) { MutableLiveData(it.verticalAccuracyMeters.toString()) }
    val speedAccuracy: LiveData<String> = switchMap(_location) { MutableLiveData(it.speedAccuracyMetersPerSecond.toString()) }
    val bearingAccuracy: LiveData<String> = switchMap(_location) { MutableLiveData(it.bearingAccuracyDegrees.toString()) }
    val time: LiveData<String> = switchMap(_location) { MutableLiveData(it.time.toString()) }
    val elapsedRealTime: LiveData<String> = switchMap(_location) { MutableLiveData(it.elapsedRealtimeNanos.toString()) }

    val acceleration: LiveData<String> = switchMap(_location) { MutableLiveData(it.acceleration.toString()) }
    val satellites: LiveData<String> = switchMap(_location) { MutableLiveData(it.satellites.toString()) }

    fun startStopLocationUpdates() {
        if (_isLocationListenerStarted.value == true) {
            stopLocationUpdates()
            return
        }

        _events.value = Event(GpsFragmentVMEventStateChange.RequestLocationUpdatesPermissions)
    }

    @SuppressLint("MissingPermission")
    fun onPermissionSuccess() {
        _startTime = Calendar.getInstance().time.time
        locationProvider
            .setInterval(_interval)
            .setPriority(LocationProvider.Priority.HIGH)
            .requestLocationUpdates(_locationCallback, Looper.getMainLooper())
        _isLocationListenerStarted.value = true
    }

    private fun stopLocationUpdates() {
        locationProvider.stopLocationUpdates()
        _isLocationListenerStarted.value = false
    }

}

sealed class GpsFragmentVMEventStateChange : EventStateChange {
    object RequestLocationUpdatesPermissions : GpsFragmentVMEventStateChange()
}