package com.example.testjetpack.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testjetpack.utils.livedata.Event
import kotlinx.coroutines.*
import timber.log.Timber

abstract class BaseViewModel<S : EventStateChange> : ViewModel() {

    val showProgressLiveData = MutableLiveData<Boolean>()
    val alertMessageLiveData = MutableLiveData<Throwable?>()

    val events: LiveData<Event<S>>
        get() = _events


    protected val _events = MutableLiveData<Event<S>>()
    protected val tag: String = javaClass.simpleName
    private val coroutines = mutableListOf<Deferred<*>>()

    protected fun addCoroutine(coroutine: Deferred<*>): Deferred<*> {
        coroutines.add(coroutine)
        return coroutine
    }

    override fun onCleared() {
        coroutines.forEach { it.cancel() }
        coroutines.clear()
        super.onCleared()
    }


    fun showProgress() {
        showProgressLiveData.postValue(true)
    }

    fun hideProgress() {
        showProgressLiveData.postValue(false)
    }

    fun <T : Any?> processCallAsync(
        call: () -> Deferred<T>,
        onSuccess: (T) -> Unit = { /* nothing by default*/ },
        onError: (E: Throwable?) -> Unit = { /* nothing by default*/ },
        showProgress: Boolean = false
    ): Deferred<*> {

        return addCoroutine(GlobalScope.async(Dispatchers.Main) {
            if (showProgress) {
                showProgress()
            }

            val jobResult = withContext(Dispatchers.IO) {
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
                hideProgress()
            }
        })
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