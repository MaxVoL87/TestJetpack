package com.example.testjetpack.ui.main.gps

import android.annotation.SuppressLint
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.gps.Location
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.ui.base.UnexpectedExeption
import com.example.testjetpack.utils.livedata.Event
import com.example.testjetpack.utils.toDBEntity
import com.google.android.gms.location.*
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

class GpsFragmentVM : BaseViewModel<GpsFragmentVMEventStateChange>() {

    //initial values
    private val _interval: Long = 1000
    private val _fastestInterval: Long = 1000
    private val _accelerationCalcTime: Long = 250
    private var _startTime: Long? = null

    private var _fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val _locationRequest = LocationRequest().apply {
        // Create the location request to start receiving updates
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = _interval
        fastestInterval = _fastestInterval
    }
    private val _locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val startTime = _startTime ?: throw UnexpectedExeption("onLocationResult.startTime == null")
            //if (_isLocationAvailable.value != true) return if not available - no location

            val dbLocation = locationResult.lastLocation.toDBEntity(startTime)
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

        override fun onLocationAvailability(lolacionAvailability: LocationAvailability?) {
            super.onLocationAvailability(lolacionAvailability)
            _isLocationAvailable.value = lolacionAvailability?.isLocationAvailable
        }
    }

    @Inject
    lateinit var dataRepository: IDataRepository

    init {
        MainApplication.component.inject(this)
    }

    private var accelerationTimer: Timer? = null
    private fun getAccelerationTimerTask() = object : TimerTask() {
        private var _lastCalcLocation: Location? = null
        private var _lastCalcSpeed: Float = 0.0F
        private var _lastCalcAcceleration: Float = -1.0F
        private var _lastCalcTime: Long = _accelerationCalcTime

        override fun run() {
            val acceleration: Float
            val curLocationSpeed = _curLocation?.speed ?: return //return if _curLocation == null
            val lastCalcSpeed = _lastCalcSpeed
            val lastCalcTime = _lastCalcTime

            if (_lastCalcLocation == null) {
                acceleration = 0.0F
            } else {
                if (_lastCalcLocation == _curLocation) {
                    acceleration = _lastCalcAcceleration
                    _lastCalcTime += _accelerationCalcTime
                } else {
                    _lastCalcSpeed = curLocationSpeed
                    _lastCalcTime = _accelerationCalcTime

                    acceleration = (curLocationSpeed - lastCalcSpeed) / (lastCalcTime / 1000) // (V - V0)/t
                }
            }
            _lastCalcLocation = _curLocation
            _lastCalcAcceleration = acceleration

            _acceleration.postValue((if (acceleration > 0.0) acceleration else 0.0F).toString())
            _deceleration.postValue((if (acceleration < 0.0) (abs(acceleration)) else 0.0F).toString())

        }

    }

    private var _curLocation: Location? = null
        set(value) {
            field = value
            if (isNeedToShowDiagnostic.value == true) _location.value = value
        }

    private val _location = MutableLiveData<Location>()
    private val _isLocationAvailable = MutableLiveData<Boolean>(false)
    private val _isLocationListenerStarted = MutableLiveData<Boolean>(false)
    private val _acceleration: MutableLiveData<String> = MutableLiveData()
    private val _deceleration: MutableLiveData<String> = MutableLiveData()

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
    val verticalAccuracy: LiveData<String> =
        switchMap(_location) { MutableLiveData(it.verticalAccuracyMeters.toString()) }
    val speedAccuracy: LiveData<String> =
        switchMap(_location) { MutableLiveData(it.speedAccuracyMetersPerSecond.toString()) }
    val bearingAccuracy: LiveData<String> =
        switchMap(_location) { MutableLiveData(it.bearingAccuracyDegrees.toString()) }
    val time: LiveData<String> = switchMap(_location) { MutableLiveData(it.time.toString()) }
    val elapsedRealTime: LiveData<String> = switchMap(_location) { MutableLiveData(it.elapsedRealtimeNanos.toString()) }

    val acceleration: LiveData<String>
        get() = _acceleration
    val deceleration: LiveData<String>
        get() = _deceleration

    fun startStopLocationUpdates(
        settingsClient: SettingsClient,
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        if (_isLocationListenerStarted.value == true) {
            accelerationTimer?.cancel()
            stopLocationUpdates()
            return
        }

        accelerationTimer = Timer(true).apply {
            schedule(getAccelerationTimerTask(), 0, _accelerationCalcTime)
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