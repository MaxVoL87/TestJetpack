package com.example.testjetpack.dataflow.gps

import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class LocationProvider(context: Context) {

    private val _settingsClient = LocationServices.getSettingsClient(context)
    private val _fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    /**
     * Possible values for the desired location accuracy.
     */
    enum class Priority(val value: Int) {
        /**
         * Highest possible accuracy, typically within 30m
         */
        HIGH(LocationRequest.PRIORITY_HIGH_ACCURACY),
        /**
         * Medium accuracy, typically within a city block / roughly 100m
         */
        MEDIUM(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY),
        /**
         * City-level accuracy, typically within 10km
         */
        LOW(LocationRequest.PRIORITY_LOW_POWER),
        /**
         * Variable accuracy, purely dependent on updates requested by other apps
         */
        PASSIVE(LocationRequest.PRIORITY_NO_POWER)
    }

    interface LocationServiceListener {

        fun onLocationCalculated(location: Location)

        fun onLocationAvailability(locationAvailability: LocationAvailability?)
    }


    private var _interval: Long = 1000
    private var _fastestInterval: Long = 500
    private var _priority: Int = LocationRequest.PRIORITY_NO_POWER

    private var _locationRequest: LocationRequest? = null
    private var _locationCallback: LocationCallback? = null
    private var _listener: LocationServiceListener? = null

    val isAllive: Boolean
        get() = _listener != null

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    fun requestLocationUpdates(listener: LocationServiceListener, looper: Looper): Task<Void> {
        if (isAllive) throw Exception("Service already  running")

        _listener = listener
        val request = createLocationRequest()

        // Create LocationSettingsRequest object using location request
        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(request)
            .build()

        _settingsClient.checkLocationSettings(locationSettingsRequest)

        return _fusedLocationProviderClient.requestLocationUpdates(request, createLocationCallback(), looper)
    }

    fun stopLocationUpdates(): Task<Void> {
        _listener = null
        return _fusedLocationProviderClient.removeLocationUpdates(_locationCallback)
    }

    fun setInterval(interval: Long): LocationProvider {
        _interval = interval
        _fastestInterval = interval
        return this
    }

    fun setPriority(priority: Priority): LocationProvider {
        _priority = priority.value
        return this
    }

    private fun createLocationRequest(): LocationRequest {
        _locationRequest = LocationRequest().apply {
            priority = _priority
            interval = _interval
            fastestInterval = _fastestInterval
        }
        return _locationRequest!!
    }

    private fun createLocationCallback(): LocationCallback {
        _locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (_listener == null) return
                val location = locationResult.lastLocation



                _listener?.onLocationCalculated(location)
            }

            override fun onLocationAvailability(lolacionAvailability: LocationAvailability?) {
                super.onLocationAvailability(lolacionAvailability)
                _listener?.onLocationAvailability(lolacionAvailability)
            }
        }
        return _locationCallback!!
    }

}