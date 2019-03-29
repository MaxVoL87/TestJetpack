package com.example.testjetpack.dataflow.repository

import com.example.testjetpack.models.Listing
import com.example.testjetpack.models.git.db.GitRepositoryView
import com.example.testjetpack.models.git.network.request.GitPage

interface IGitDataRepository {

    fun searchGitRepositories(page: GitPage): Listing<GitRepositoryView>
}