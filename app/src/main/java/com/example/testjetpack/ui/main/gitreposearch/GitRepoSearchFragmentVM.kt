package com.example.testjetpack.ui.main.gitreposearch

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.git.GitRepository
import com.example.testjetpack.models.git.network.GitPage
import com.example.testjetpack.models.git.network.Listing
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.UiUtils.hideKeyboard
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import javax.inject.Inject

class GitRepoSearchFragmentVM : BaseViewModel<GitRepoSearchFragmentVMEventStateChange>() {

    @Inject
    lateinit var repository: IDataRepository

    init {
        MainApplication.component.inject(this)
    }

    private val page: MutableLiveData<GitPage> =
        MutableLiveData(GitPage(number = 1, q = "", perPage = IDataRepository.DEFAULT_NETWORK_PAGE_SIZE))
    private val repoResult: MutableLiveData<Listing<GitRepository>> = MutableLiveData()

    val searchText: MutableLiveData<String?> = MutableLiveData<String?>().apply {
        observeForever { text ->
            if (text != null) {
                page.value?.let {
                    if (it.q != text)
                        page.value = it.copy(q = text)
                }
            }
        }
    }
    val adapter = GitRepoSearchAdapter { retry() }
    val repos = switchMap(repoResult) { it.pagedList }
    val networkState = switchMap(repoResult) { it.networkState }
    val refreshState = switchMap(repoResult) { it.refreshState }

    fun searchRepos(view: View?) {
        view?.let { hideKeyboard(it) }

        val mPage = page.value ?: return
        adapter.submitList(null)

        processCallAsync(
            call = {
                GlobalScope.async {
                    repository.getGitRepositories(page = mPage)
                }
            },
            onSuccess = { listing ->
                repoResult.value = listing
            },
            onError = {
                onError(it)
            },
            showProgress = true
        )
    }


    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = repoResult.value
        listing?.retry?.invoke()
    }
}

sealed class GitRepoSearchFragmentVMEventStateChange : EventStateChange {
}