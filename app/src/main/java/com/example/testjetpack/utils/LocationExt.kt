package com.example.testjetpack.utils

import android.location.Location

fun Location.calculateAcceleration(prevSpeed: Float, prevTime: Long): Float {
    val speedDiff = speed - prevSpeed
    val timeDiff = (time - prevTime).toFloat() * 0.001F //milliseconds to seconds

    return if (speedDiff == 0.0F || timeDiff == 0.0F) 0.0F
    else speedDiff / timeDiff // (V - V0)/t
}