package com.example.testjetpack.utils

import java.lang.Float.floatToIntBits
import java.lang.Float.intBitsToFloat
import java.util.concurrent.atomic.AtomicInteger


class AtomicFloat(initialValue: Float = 0f) : Number() {

    private val bits: AtomicInteger = AtomicInteger(floatToIntBits(initialValue))

    fun compareAndSet(expect: Float, update: Float): Boolean {
        return bits.compareAndSet(
            floatToIntBits(expect),
            floatToIntBits(update)
        )
    }

    fun set(newValue: Float) {
        bits.set(floatToIntBits(newValue))
    }

    fun get(): Float {
        return intBitsToFloat(bits.get())
    }

    fun getAndSet(newValue: Float): Float {
        return intBitsToFloat(bits.getAndSet(floatToIntBits(newValue)))
    }

    fun weakCompareAndSet(expect: Float, update: Float): Boolean {
        return bits.weakCompareAndSet(
            floatToIntBits(expect),
            floatToIntBits(update)
        )
    }

    override fun toByte(): Byte {
        return toInt().toByte()
    }

    override fun toChar(): Char {
        return toInt().toChar()
    }

    override fun toDouble(): Double {
        return toFloat().toDouble()
    }

    override fun toFloat(): Float {
        return get()
    }

    override fun toInt(): Int {
        return get().toInt()
    }

    override fun toLong(): Long {
        return get().toLong()
    }

    override fun toShort(): Short {
        return toInt().toShort()
    }
}