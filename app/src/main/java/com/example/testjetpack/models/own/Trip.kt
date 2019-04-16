package com.example.testjetpack.models.own

import com.google.android.gms.maps.model.LatLng

data class Trip(
    val locations: List<LatLng>
)