package com.example.testjetpack.ui.main.gitreposearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.git.GitRepository
import com.example.testjetpack.models.git.network.GitPage
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import javax.inject.Inject

class GitRepoSearchFragmentVM : BaseViewModel<GitRepoSearchFragmentVMEventStateChange>() {

    @Inject
    lateinit var repository: IDataRepository

    init {
        MainApplication.component.inject(this)
    }

    val searchText: MutableLiveData<String?> = MutableLiveData()
    val foundRepos: LiveData<PagedList<GitRepository>>
        get() = _foundRepos

    private var _foundRepos: LiveData<PagedList<GitRepository>> = MutableLiveData()
    private val page: MutableLiveData<GitPage> = MutableLiveData()

    fun searchRepos() {
        val _searchString = searchText.value ?: return
        val _page = GitPage(number = 1, q = _searchString, perPage = IDataRepository.DEFAULT_NETWORK_PAGE_SIZE)
        page.value = _page

        processCallAsync(
            call = {
                GlobalScope.async {
                    repository.getGitRepositories(page = _page)
                }
            },
            onSuccess = { listing ->
                _foundRepos = listing.pagedList
            },
            onError = {
                onError(it)
            },
            showProgress = true
        )
    }
}

sealed class GitRepoSearchFragmentVMEventStateChange : EventStateChange {
}