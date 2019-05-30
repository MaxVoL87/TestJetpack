package com.example.testjetpack.ui.main.gitrepodetails

import android.graphics.drawable.Drawable
import androidx.collection.LruCache
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import com.example.testjetpack.dataflow.repository.IGitDataRepository
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.livedata.toLiveData
import com.example.testjetpack.utils.toMapOfStrings
import com.example.testjetpack.utils.withNotNull
import com.squareup.picasso.Picasso

class GitRepoDetailsFragmentVM(
    val picasso: Picasso,
    private val _cache: LruCache<String, Any>,
    private val _gitDataRepository: IGitDataRepository
) : BaseViewModel<GitRepoDetailsFragmentVMEventStateChange>() {

    private val _repoDetails = MutableLiveData<Map<String, String>>()

    val gitRepoDetailsFragmentArgs = MutableLiveData<GitRepoDetailsFragmentArgs>()

    val avatarUrl = switchMap(gitRepoDetailsFragmentArgs) { it?.gitRepositoryView?.avatarUrl?.toLiveData() }
    val transitionName = switchMap(gitRepoDetailsFragmentArgs) { it?.transitionName?.toLiveData() }
    val transitionDrawable = switchMap(avatarUrl) { it?.let { url -> _cache.get(url) as? Drawable }?.toLiveData() }
    val repoDetails: LiveData<Map<String, String>> = switchMap(gitRepoDetailsFragmentArgs) { it?.gitRepositoryView?.index?.let { idx -> getRepository(idx) } }

    private fun getRepository(index: Int): LiveData<Map<String, String>> {
        processCallAsync(
            call = { _gitDataRepository.getGitRepositoryFromCache(index).toMapOfStrings() },
            onSuccess = { _repoDetails.value = it },
            onError = { showAlert(it) }
        )
        return _repoDetails
    }

    override fun onCleared() {
        withNotNull(avatarUrl.value) {
            _cache.remove(this)
        }
        super.onCleared()
    }
}

sealed class GitRepoDetailsFragmentVMEventStateChange : EventStateChange