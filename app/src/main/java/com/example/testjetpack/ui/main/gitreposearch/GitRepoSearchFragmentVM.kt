package com.example.testjetpack.ui.main.gitreposearch

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.navigation.fragment.FragmentNavigator
import com.example.testjetpack.MainApplicationContract
import com.example.testjetpack.dataflow.repository.IGitDataRepository
import com.example.testjetpack.models.git.network.request.GitPage
import com.example.testjetpack.models.Listing
import com.example.testjetpack.models.git.db.GitRepositoryView
import com.example.testjetpack.ui.base.BaseRecyclerItemViewModel
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.OnEditorOk
import com.example.testjetpack.utils.UiUtils.hideKeyboard
import com.example.testjetpack.utils.livedata.Event
import com.example.testjetpack.utils.livedata.toMutableLiveData
import com.example.testjetpack.utils.reset

class GitRepoSearchFragmentVM(
    private val gitDataRepository: IGitDataRepository
) : BaseViewModel<GitRepoSearchFragmentVMEventStateChange>() {

    private val _page: MutableLiveData<GitPage> = GitPage(
        page = 1,
        q = "",
        perPage = MainApplicationContract.DEFAULT_NETWORK_PAGE_SIZE
    ).toMutableLiveData()

    private val _repoResult: MutableLiveData<Listing<GitRepositoryView>> = MutableLiveData()
    private val _scrollToPosition: MutableLiveData<Int> = MutableLiveData(-1)

    val searchText: MutableLiveData<String> = MutableLiveData<String>().apply {
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

    val adapter = GitRepoSearchAdapter(::retry).apply {
        setOnOpenRepositoryListener(::openGitRepository)
        setOnItemClickListener(object : GitRepoSearchAdapter.OnItemClickListener<BaseRecyclerItemViewModel> {
            override fun onItemClick(position: Int, item: BaseRecyclerItemViewModel) {
                if (item is GitRepoSearchItemVM) {
                    _events.value = Event(GitRepoSearchFragmentVMEventStateChange.ShowGitRepository(item.repo, item.fragmentNavigatorExtras))
                }
            }
        })
    }
    val repos = switchMap(_repoResult) { it.pagedList }
    val networkState = switchMap(_repoResult) { it.networkState }
    val refreshState = switchMap(_repoResult) { it.refreshState }

    val scrollToPosition: LiveData<Int>
        get() = _scrollToPosition

    fun searchRepos(view: View?) {
        view?.let { hideKeyboard(it) }

        _scrollToPosition.value = 0
        adapter.submitList(null)

        var mPage = _page.value ?: return
        mPage = mPage.reset()
        _page.value = mPage

        processCallAsync(
            call = { gitDataRepository.searchGitRepositories(page = mPage) },
            onSuccess = { listing ->
                _scrollToPosition.value = -1
                _repoResult.value = listing
            },
            onError = {
                onError(it)
            },
            showProgress = false
        )
    }


    fun refresh() {
        _repoResult.value?.refresh?.invoke()
    }

    private fun retry() {
        val listing = _repoResult.value
        listing?.retry?.invoke()
    }

    private fun openGitRepository(repoUrl: String) {
        _events.value = Event(GitRepoSearchFragmentVMEventStateChange.OpenGitRepository(repoUrl))
    }
}

sealed class GitRepoSearchFragmentVMEventStateChange : EventStateChange {
    class OpenGitRepository(val repoUrl: String) : GitRepoSearchFragmentVMEventStateChange()
    class ShowGitRepository(val gitRepository: GitRepositoryView, val fragmentNavigatorExtras: FragmentNavigator.Extras? = null) : GitRepoSearchFragmentVMEventStateChange()
}