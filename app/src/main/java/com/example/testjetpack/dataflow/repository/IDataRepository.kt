package com.example.testjetpack.dataflow.repository

import com.example.testjetpack.models.GitRepositoryView
import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.models.own.Profile
import com.example.testjetpack.models.git.network.GitPage
import com.example.testjetpack.models.git.network.Listing
import kotlinx.coroutines.Deferred

interface IDataRepository {

    fun getProfileAsync(): Deferred<Profile>

    fun getNotificationsAsync(): Deferred<List<Notification>>

    fun getGitRepositories(page: GitPage): Listing<GitRepositoryView>

    companion object {
        const val DEFAULT_NETWORK_PAGE_SIZE = 10
    }
}