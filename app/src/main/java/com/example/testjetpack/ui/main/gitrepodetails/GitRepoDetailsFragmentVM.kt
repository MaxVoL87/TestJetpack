package com.example.testjetpack.ui.main.gitrepodetails

import android.content.res.Resources
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.livedata.toLiveData
import com.squareup.picasso.Picasso

class GitRepoDetailsFragmentVM(
    val resources: Resources,
    val picasso: Picasso
) : BaseViewModel<GitRepoDetailsFragmentVMEventStateChange>() {

    val gitRepoDetailsFragmentArgs = MutableLiveData<GitRepoDetailsFragmentArgs>()

    val transitionName = switchMap(gitRepoDetailsFragmentArgs) { it.transitionName.toLiveData() }
    val transitionDrawable = switchMap(gitRepoDetailsFragmentArgs) { it.transitionBitmap?.toDrawable(resources).toLiveData() }
    val avatarUrl = switchMap(gitRepoDetailsFragmentArgs) { it.gitRepositoryView.avatarUrl.toLiveData() }
    val gitRepository = switchMap(gitRepoDetailsFragmentArgs) { it.gitRepositoryView.toLiveData() }

}

sealed class GitRepoDetailsFragmentVMEventStateChange : EventStateChange