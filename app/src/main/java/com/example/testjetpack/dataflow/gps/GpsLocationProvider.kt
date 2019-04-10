package com.example.testjetpack.dataflow.gps

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.*
import android.location.GpsStatus.*
import androidx.annotation.RequiresPermission
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.testjetpack.utils.calculateAcceleration
import com.example.testjetpack.utils.withNotNull

/**
 * Location provider based on Gps only
 * https://stackoverflow.com/questions/26148235/getting-satellites-in-view-and-satellites-in-use-counts-in-android
 */
class GpsLocationProvider(mContext: Context) {

    private val locationManager: LocationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager

    private var _gpsStatus: GpsStatus? = null
    private var _gnssStatusCallback: GnssStatus.Callback? = null
    private var _locationListener: LocationListener? = null
    private var _gpsListener: GpsStatus.Listener? = null

    private var _listener: ILocationProviderListener? = null
    private var _lastCalcLocation: Location? = null
    private var _satellitesInUse: Int = 0
    private var _satellitesInView: Int = 0

    private val _minDistanceChangeForUpdates: Float = 0.0F // 0 meters
    private var _interval: Long = 1000

    val isAlive: Boolean
        get() = _listener != null

    @RequiresPermission(ACCESS_FINE_LOCATION)
    fun requestLocationUpdates(listener: ILocationProviderListener) {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) throw Exception("GPS not enabled")
        _listener = listener

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locationManager.registerGnssStatusCallback(getGnssCallback())
        } else {
            locationManager.addGpsStatusListener(getGpsListener())
            locationManager.getGpsStatus(null)
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            _interval,
            _minDistanceChangeForUpdates,
            getLocationListener()
        )
    }

    fun stopLocationUpdates() {
        _listener = null
        locationManager.removeUpdates(_locationListener)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locationManager.unregisterGnssStatusCallback(_gnssStatusCallback)
        } else {
            locationManager.removeGpsStatusListener(_gpsListener)
        }
        resetCalculations()
    }

    fun setInterval(interval: Long): GpsLocationProvider {
        _interval = interval
        return this
    }

    private fun resetCalculations() {
        _lastCalcLocation = null
        _satellitesInUse = 0
        _satellitesInView = 0
    }

    private fun getLocationListener(): LocationListener {
        _locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                _listener?.onLocationAvailable(location != null)
                if(location == null) return

                val lastCalcLocation = _lastCalcLocation
                val acceleration =
                    if (lastCalcLocation != null) location.calculateAcceleration(lastCalcLocation.speed, lastCalcLocation.time)
                    else 0.0F

                location.extras.putFloat(acceleration_extra, acceleration)
                location.extras.putInt(satellites_extra, location.extras.getInt("satellites", -1))

                _lastCalcLocation = location

                _listener?.onLocationCalculated(location)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }

        }

        return _locationListener!!
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getGnssCallback(): GnssStatus.Callback {
        _gnssStatusCallback = object : GnssStatus.Callback() {
            override fun onSatelliteStatusChanged(status: GnssStatus?) {
                super.onSatelliteStatusChanged(status)
                var satellitesCount = 0
                var usedInFixCount = 0
                withNotNull(status) {
                    satellitesCount = satelliteCount
                    for (i in 0..(satelliteCount - 1)) {
                        if (usedInFix(i)) usedInFixCount++
                    }
                }
                _satellitesInView = satellitesCount
                _satellitesInUse = usedInFixCount
            }

            override fun onStarted() {
                super.onStarted()
            }

            override fun onFirstFix(ttffMillis: Int) {
                super.onFirstFix(ttffMillis)
            }

            override fun onStopped() {
                super.onStopped()
            }
        }
        return _gnssStatusCallback!!
    }

    @RequiresPermission(ACCESS_FINE_LOCATION)
    private fun getGpsListener(): GpsStatus.Listener {
        _gpsListener = GpsStatus.Listener { event ->
            when (event) {
                GPS_EVENT_STARTED -> {
                }
                GPS_EVENT_STOPPED -> {
                }
                GPS_EVENT_FIRST_FIX -> {
                }
                GPS_EVENT_SATELLITE_STATUS -> {
                    _gpsStatus = locationManager.getGpsStatus(_gpsStatus)
                    val satellites = _gpsStatus!!.satellites.toList()
                    _satellitesInView = satellites.size
                    _satellitesInUse = satellites.count { it.usedInFix() }
                }
            }
        }
        return _gpsListener!!
    }
}