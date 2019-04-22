package com.example.testjetpack.ui.main.gitreposearch

import com.example.testjetpack.models.git.db.GitRepositoryView
import com.example.testjetpack.ui.base.ICallback

interface IGitRepoSearchFragmentCallback : ICallback {

    fun openGitRepository(repo: GitRepositoryView)
}