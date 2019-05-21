package com.example.testjetpack.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testjetpack.utils.livedata.Event
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<S : EventStateChange> : ViewModel(), CoroutineScope {

    val showProgressLiveData = MutableLiveData<Boolean>()
    val alertMessageLiveData = MutableLiveData<Throwable?>()

    val events: LiveData<Event<S>>
        get() = _events

    protected val _events = MutableLiveData<Event<S>>()
    protected val tag: String = javaClass.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job: Job = Job()

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }

    fun <T : Any?> processCallAsync(
        call: () -> T,
        onSuccess: (T) -> Unit = { /* nothing by default*/ },
        onError: (E: Throwable?) -> Unit = { /* nothing by default*/ },
        showProgress: Boolean = false
    ): Deferred<*> {

        return async {
            if (showProgress) {
                showProgressLiveData.value = true
            }

            val jobResult = GlobalScope.async(Dispatchers.IO) {
                call()
            }

            if (isActive) {
                try {
                    onSuccess(jobResult.await())
                } catch (t: Throwable) {
                    onError(t)
                }
            }

            if (showProgress) {
                showProgressLiveData.value = false
            }
        }
    }

    protected fun onError(throwable: Throwable?) {
        Timber.e("Error $throwable")
        showAlert(throwable)
    }

    fun showAlert(throwable: Throwable? = null) {
        alertMessageLiveData.value = throwable
    }
}

interface EventStateChange