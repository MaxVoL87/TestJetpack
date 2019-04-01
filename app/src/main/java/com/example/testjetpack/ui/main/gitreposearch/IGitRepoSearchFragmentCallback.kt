package com.example.testjetpack.ui.main.gitreposearch

import com.example.testjetpack.models.git.db.GitRepositoryView

interface IGitRepoSearchFragmentCallback {

    fun openGitRepository(repo: GitRepositoryView)
}