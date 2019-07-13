package com.example.testjetpack.ui.main.mystatus

import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.own.Profile
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.livedata.toMutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


class MyStatusFragmentVM(private val _dataRepository: IDataRepository) : BaseViewModel<MyStatusFragmentVMEventStateChange>() {

    private val _profile = MutableLiveData<Profile>()
    private val _somePercents = 0.0F.toMutableLiveData()
    private val _profileLoaded: Boolean
        get() = _profile.value != null

    val animationProgress = 0.0F.toMutableLiveData()

    val vpAdapter = MyStatusViewPagerAdapter().apply {
        itemViewModels = mutableListOf(
            ArcCharItemViewModel(_profile, _somePercents).apply {
                this@MyStatusFragmentVM.animationProgress.observeForever { this.setAnimationProgress(it) }
            },
            InfoItemViewModel(_profile, animationProgress)
        )
    }

    fun getProfile() {

        GlobalScope.launch {
            var percents = 0
            while (!_profileLoaded) {
                delay(1000)
                percents += Random.nextInt(20, 40)
                val perc = percents.div(100F)
                _somePercents.postValue(if(perc > 1F) 1F else perc)
            }
            delay(1000)
            _somePercents.postValue(1.0F)
        }

        processCallAsync(
            call = { _dataRepository.getProfile() },
            onSuccess = { profile ->
                _profile.postValue(profile)
            },
            onError = {
                _profile.postValue(null)
                onError(it)
            },
            showProgress = false
        )
    }
}

sealed class MyStatusFragmentVMEventStateChange : EventStateChange