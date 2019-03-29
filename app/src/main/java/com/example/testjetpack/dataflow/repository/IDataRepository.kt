package com.example.testjetpack.dataflow.repository

import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.models.own.Profile

interface IDataRepository {

    fun getProfileAsync(): Profile

    fun getNotificationsAsync(): List<Notification>
}