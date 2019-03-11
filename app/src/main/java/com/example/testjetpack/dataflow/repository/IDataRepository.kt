package com.example.testjetpack.dataflow.repository

import com.example.testjetpack.models.Notification
import com.example.testjetpack.models.Profile
import kotlinx.coroutines.Deferred

interface IDataRepository {

    fun getProfileAsync() : Deferred<Profile>

    fun getNotificationsAsync() : Deferred<List<Notification>>

}