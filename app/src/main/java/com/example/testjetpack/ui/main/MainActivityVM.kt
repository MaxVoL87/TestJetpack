package com.example.testjetpack.ui.main

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.MainApplication
import com.example.testjetpack.R
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.own.Profile
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.livedata.Event
import com.google.android.material.navigation.NavigationView
import javax.inject.Inject

class MainActivityVM : BaseViewModel<MainActivityVMEventStateChange>(),
    NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var repository: IDataRepository

    init {
        MainApplication.component.inject(this)
    }

    val profile: LiveData<Profile?>
        get() = _profile

    private val _profile: MutableLiveData<Profile?> = MutableLiveData()

    // region OnNavigationItemSelectedListener impl
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        _events.value = Event(MainActivityVMEventStateChange.CloseDrawer)

        var event: MainActivityVMEventStateChange? = null
        when (item.itemId) {
            R.id.nav_my_profile -> {
                event = MainActivityVMEventStateChange.OpenProfile
            }
            R.id.nav_gps -> {
                event = MainActivityVMEventStateChange.OpenGps
            }
            R.id.nav_manage -> {

            }

            R.id.nav_notifications -> {
                event = MainActivityVMEventStateChange.OpenNotifications
            }
            R.id.nav_provide_feedback -> {

            }

            R.id.nav_log_out -> {

            }
        }

        if(event == null) return false

        _events.value = Event(event)
        return true
    }
    // endregion OnNavigationItemSelectedListener impl

    fun getProfile() {
        processCallAsync(
            call = { repository.getProfile() },
            onSuccess = { profile ->
                _profile.postValue(profile)
            },
            onError = {
                _profile.postValue(null)
                onError(it)
            },
            showProgress = true
        )
    }
}

sealed class MainActivityVMEventStateChange : EventStateChange {
    object CloseDrawer : MainActivityVMEventStateChange()
    object OpenProfile : MainActivityVMEventStateChange()
    object OpenGps : MainActivityVMEventStateChange()
    object OpenNotifications : MainActivityVMEventStateChange()
}