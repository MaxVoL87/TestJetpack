package com.example.testjetpack.dataflow.repository

import com.example.testjetpack.models.Profile
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class DataRepository : IDataRepository {

    override fun getProfileAsync(): Deferred<Profile> {
        //todo: change with api
        return GlobalScope.async { Profile("https://picsum.photos/200/200/?random", "Benjamin", "Bankog", "11.07.1975", "MORGA753116SM9IJ", "MQQ346789C") }
    }

}