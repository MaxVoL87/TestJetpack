package com.example.testjetpack.dataflow.repository

import com.example.testjetpack.models.Notification
import com.example.testjetpack.models.Profile
import com.example.testjetpack.models.git.GitRepository
import com.example.testjetpack.models.git.network.GitPage
import com.example.testjetpack.models.git.network.Listing
import kotlinx.coroutines.Deferred

interface IDataRepository {

    fun getProfileAsync(): Deferred<Profile>

    fun getNotificationsAsync(): Deferred<List<Notification>>

    fun getGitRepositories(page: GitPage): Listing<GitRepository>

    companion object {
        public const val DEFAULT_NETWORK_PAGE_SIZE = 10
    }
}