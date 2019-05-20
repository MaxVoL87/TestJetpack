package com.example.testjetpack.ui.splash

import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.livedata.Event
import java.lang.Exception
import kotlin.random.Random

class SplashActivityVM : BaseViewModel<SplashActivityVMEventStateChange>() {

    fun checkSingInState() {
        processCallAsync(
            call = {
                val toValue = Random.nextInt( 2, 5)
                for (i in 0 .. toValue){
                    Thread.sleep(1000)
                    if(i == toValue && i % 2 != 0) throw Exception("Need Login")
                }
            },
            onSuccess = {
                _events.value = Event(SplashActivityVMEventStateChange.OpenMain)
            },
            onError = {
                _events.value = Event(SplashActivityVMEventStateChange.OpenSingIn)
            }
        )
    }
}

sealed class SplashActivityVMEventStateChange : EventStateChange {
    object OpenSingIn : SplashActivityVMEventStateChange()
    object OpenMain : SplashActivityVMEventStateChange()
}