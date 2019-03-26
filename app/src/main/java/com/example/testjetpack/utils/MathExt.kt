package com.example.testjetpack.utils

import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Returns value from 0 to thisValue, which represents percent of this value
 *
 * @param part value from 0.0 to 1.0; represents 0% - 100%
 *
 * @return percent of value
 */
fun Int.getPartOfOrCurrent(part: Double): Int{
    return min(max(0, this), max(1, (this * part).roundToInt()))
}
