package com.example.testjetpack.mock

import com.example.testjetpack.models.own.Location
import com.example.testjetpack.models.own.Trip
import com.example.testjetpack.utils.fromJson
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

val mockTripLatLng = "[" +
        "{\"latitude\":39.877812,\"longitude\":-86.122479}," +
        "{\"latitude\":39.891391,\"longitude\":-86.121339}," +
        "{\"latitude\":39.904824,\"longitude\":-86.121168}," +
        "{\"latitude\":39.910091,\"longitude\":-86.116533}," +
        "{\"latitude\":39.911671,\"longitude\":-86.106748}," +
        "{\"latitude\":39.908248,\"longitude\":-86.09559}," +
        "{\"latitude\":39.905877,\"longitude\":-86.081686}," +
        "{\"latitude\":39.905351,\"longitude\":-86.069841}," +
        "{\"latitude\":39.905351,\"longitude\":-86.055593}," +
        "{\"latitude\":39.905087,\"longitude\":-86.044778}," +
        "{\"latitude\":39.896659,\"longitude\":-86.045293}," +
        "{\"latitude\":39.891391,\"longitude\":-86.046152}," +
        "{\"latitude\":39.885859,\"longitude\":-86.045637}," +
        "{\"latitude\":39.880326,\"longitude\":-86.044778}," +
        "{\"latitude\":39.87453,\"longitude\":-86.04495}," +
        "{\"latitude\":39.86926,\"longitude\":-86.045808}," +
        "{\"latitude\":39.863463,\"longitude\":-86.045293}," +
        "{\"latitude\":39.85582,\"longitude\":-86.046152}," +
        "{\"latitude\":39.854239,\"longitude\":-86.05731}," +
        "{\"latitude\":39.855557,\"longitude\":-86.070528}" +
        "]"

val locations = {
    var time = 1L
    Gson().fromJson<List<LatLng>>(mockTripLatLng)!!.map {
        time += 5000
        Location(
            0,
            0.0,
            it.latitude,
            it.longitude,
            0F,
            0F,
            0F,
            0F,
            0F,
            0F,
            time,
            0L,
            0F,
            0
        )
    }
}.invoke()

val trip = Trip(locations)