package com.example.testjetpack.ui.main.gitreposearch

import androidx.navigation.fragment.FragmentNavigator
import com.example.testjetpack.models.git.db.GitRepositoryView
import com.example.testjetpack.ui.base.ICallback

interface IGitRepoSearchFragmentCallback : ICallback {

    fun openGitRepository(repoUrl: String)

    fun showGitRepository(repo: GitRepositoryView, fragmentNavigatorExtras: FragmentNavigator.Extras? = null)
}