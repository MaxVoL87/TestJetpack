package com.example.testjetpack.models.own

import androidx.room.*
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "location_table", indices = [Index(value = ["id"]), Index(value = ["start_time"])])
data class Location(
    @ColumnInfo(name = "start_time") val startTime: Long,

    val altitude: Double,
    val latitude: Double,
    val longitude: Double,
    val bearing: Float,
    val speed: Float,
    val accuracy: Float, // horizontal
    @ColumnInfo(name = "vertical_accuracy_meters") val verticalAccuracyMeters: Float,
    @ColumnInfo(name = "speed_accuracy_meters_per_second") val speedAccuracyMetersPerSecond: Float,
    @ColumnInfo(name = "bearing_accuracy_degrees") val bearingAccuracyDegrees: Float,
    val time: Long,
    @ColumnInfo(name = "elapsed_realtime_nanos") val elapsedRealtimeNanos: Long,

    val acceleration: Float,
    val satellites: Int
){
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0

    @delegate:Ignore val latLng: LatLng by lazy { LatLng(latitude, longitude) }
}