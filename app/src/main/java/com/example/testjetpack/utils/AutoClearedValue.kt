package com.example.testjetpack.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A lazy property that gets cleaned up when the lifecycleOwner is destroyed.
 *
 * Accessing this variable in a destroyed fragment will throw NPE.
 * @see {@link https://github.com/googlesamples/android-architecture-components/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/util/AutoClearedValue.kt}
 */
class AutoClearedValue<T : Any>(lifecycleOwner: LifecycleOwner) : ReadWriteProperty<LifecycleOwner, T> {
    private var _value: T? = null

    init {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                _value = null
            }
        })
    }

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
        return _value ?: throw IllegalStateException(
            "should never call auto-cleared-value get when it might not be available"
        )
    }

    override fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: T) {
        _value = value
    }
}

/**
 * Creates an [AutoClearedValue] associated with this any LifecycleOwner.
 */
fun <T : Any> LifecycleOwner.autoCleared() = AutoClearedValue<T>(this)