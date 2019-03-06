package com.example.testjetpack.ui.main

import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.MainApplication
import com.example.testjetpack.R
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.Profile
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.livedata.Event
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
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

        when (item.itemId) {
            R.id.nav_my_profile -> {
                _events.value = Event(MainActivityVMEventStateChange.OpenProfile)
            }
            R.id.nav_my_history -> {

            }
            R.id.nav_manage -> {

            }

            R.id.nav_tutorials -> {

            }
            R.id.nav_provide_feedback -> {

            }

            R.id.nav_log_out -> {

            }
        }
        return true
    }
    // endregion OnNavigationItemSelectedListener impl

    fun getProfile() {
        processAsyncCall(
            call = { repository.getProfileAsync() },
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
}