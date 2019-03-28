package com.example.testjetpack.dataflow.repository

import com.example.testjetpack.models.GitRepositoryComplexView
import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.models.own.Profile
import com.example.testjetpack.models.git.network.request.GitPage
import com.example.testjetpack.models.Listing
import kotlinx.coroutines.Deferred

interface IDataRepository {

    fun getProfileAsync(): Deferred<Profile>

    fun getNotificationsAsync(): Deferred<List<Notification>>

    fun searchGitRepositories(page: GitPage): Listing<GitRepositoryComplexView>

    companion object {
        const val DEFAULT_NETWORK_PAGE_SIZE = 10
    }
}