package com.example.testjetpack.ui.main.gitreposearch

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.git.GitRepository
import com.example.testjetpack.models.git.network.GitPage
import com.example.testjetpack.models.git.network.Listing
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.OnEditorOk
import com.example.testjetpack.utils.UiUtils.hideKeyboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class GitRepoSearchFragmentVM : BaseViewModel<GitRepoSearchFragmentVMEventStateChange>() {

    @Inject
    lateinit var repository: IDataRepository

    init {
        MainApplication.component.inject(this)
    }

    private val _page: MutableLiveData<GitPage> =
        MutableLiveData(GitPage(number = AtomicInteger(1), q = "", perPage = IDataRepository.DEFAULT_NETWORK_PAGE_SIZE))
    private val _repoResult: MutableLiveData<Listing<GitRepository>> = MutableLiveData()
    private val _scrollToPosition: MutableLiveData<Int> = MutableLiveData(0)

    val searchText: MutableLiveData<String?> = MutableLiveData<String?>().apply {
        observeForever { text ->
            if (text != null) {
                _page.value?.let {
                    if (it.q != text)
                        _page.value = it.copy(q = text)
                }
            }
        }
    }
    val onOk = object : OnEditorOk() {
        override fun onOk(view: View?) {
            searchRepos(view)
        }
    }

    val adapter = GitRepoSearchAdapter { retry() }
    val repos = switchMap(_repoResult) { it.pagedList }
    val networkState = switchMap(_repoResult) { it.networkState }
    val refreshState = switchMap(_repoResult) { it.refreshState }

    val scrollToPosition: LiveData<Int>
        get() = _scrollToPosition

    fun searchRepos(view: View?) {
        view?.let { hideKeyboard(it) }

        _scrollToPosition.value = 0
        adapter.submitList(null)

        val mPage = _page.value ?: return
        mPage.number.set(1)


        processCallAsync(
            call = {
                GlobalScope.async(Dispatchers.Unconfined) {
                    repository.getGitRepositories(page = mPage)
                }
            },
            onSuccess = { listing ->
                _repoResult.value = listing
            },
            onError = {
                onError(it)
            },
            showProgress = true
        )
    }


    fun refresh() {
        _repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = _repoResult.value
        listing?.retry?.invoke()
    }
}

sealed class GitRepoSearchFragmentVMEventStateChange : EventStateChange {
}