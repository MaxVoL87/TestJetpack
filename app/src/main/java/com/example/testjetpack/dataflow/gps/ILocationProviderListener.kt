package com.example.testjetpack.dataflow.gps

import android.location.Location

interface ILocationProviderListener {

    fun onLocationCalculated(location: Location)

    fun onLocationAvailable(isAvailable: Boolean)
}

const val acceleration_extra = "acceleration_extra"
const val satellites_extra = "satellites_extra"