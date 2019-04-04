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


class GpsLocationProvider(mContext: Context) {

    private val locationManager: LocationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager

    private var mGpsStatus: GpsStatus? = null
    private var mGnssStatusCallback: GnssStatus.Callback? = null
    private var _locationListener: LocationListener? = null
    private var _gpsListener: GpsStatus.Listener? = null

    private var _listener: ILocationProviderListener? = null
    private var _lastCalcLocation: Location? = null
    private var szSatellitesInUse: Int = 0
    private var szSatellitesInView: Int = 0

    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 0.0F // 0 meters
    private var _interval: Long = 1000

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
            MIN_DISTANCE_CHANGE_FOR_UPDATES,
            getLocationListener()
        )
    }

    fun stopLocationUpdates() {
        _listener = null
        locationManager.removeUpdates(_locationListener)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locationManager.unregisterGnssStatusCallback(mGnssStatusCallback)
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
        szSatellitesInUse = 0
        szSatellitesInView = 0
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
                location.extras.putInt(satellites_extra, szSatellitesInUse)
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
        mGnssStatusCallback = object : GnssStatus.Callback() {
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
                szSatellitesInView = satellitesCount
                szSatellitesInUse = usedInFixCount
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
        return mGnssStatusCallback!!
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
                    mGpsStatus = locationManager.getGpsStatus(mGpsStatus)
                    val satellites = mGpsStatus!!.satellites.toList()
                    szSatellitesInView = satellites.size
                    szSatellitesInUse = satellites.count { it.usedInFix() }
                }
            }
        }
        return _gpsListener!!
    }
}