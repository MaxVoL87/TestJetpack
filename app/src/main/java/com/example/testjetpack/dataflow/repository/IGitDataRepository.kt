package com.example.testjetpack.dataflow.repository

import com.example.testjetpack.models.Listing
import com.example.testjetpack.models.git.db.GitRepositoryView
import com.example.testjetpack.models.git.network.request.GitPage

interface IGitDataRepository {

    fun getGitRepositoryFromCache(indexInResponse: Int): com.example.testjetpack.models.git.db.GitRepository

    fun searchGitRepositories(page: GitPage): Listing<GitRepositoryView>
}