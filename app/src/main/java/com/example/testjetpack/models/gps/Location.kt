package com.example.testjetpack.models.gps

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "location_table", indices = [Index(value = ["id"]), Index(value = ["start_time"])])
data class Location(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "start_time") val startTime: Long,
    val accuracy: Float,
    val altitude: Double,
    val latitude: Double,
    val longitude: Double,
    val bearing: Float,
    val speed: Float,
    val time: Long,

    @ColumnInfo(name = "vertical_accuracy_meters") val verticalAccuracyMeters: Float,
    @ColumnInfo(name = "speed_accuracy_meters_per_second") val speedAccuracyMetersPerSecond: Float,
    @ColumnInfo(name = "bearing_accuracy_degrees") val bearingAccuracyDegrees: Float,
    @ColumnInfo(name = "elapsed_realtime_nanos") val elapsedRealtimeNanos: Long,

    @ColumnInfo(name = "describe_contents") val describeContents: Int
)