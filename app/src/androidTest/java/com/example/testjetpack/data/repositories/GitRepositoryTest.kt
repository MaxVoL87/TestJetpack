package com.example.testjetpack.data.repositories

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testjetpack.dataflow.repository.IGitDataRepository
import org.junit.runner.RunWith
import org.koin.core.KoinComponent
import org.koin.core.inject

//@RunWith(AndroidJUnit4::class)
class GitRepositoryTest : KoinComponent {

    private val gitRepository: IGitDataRepository by inject()

    //todo: need to create mock data for testing
}