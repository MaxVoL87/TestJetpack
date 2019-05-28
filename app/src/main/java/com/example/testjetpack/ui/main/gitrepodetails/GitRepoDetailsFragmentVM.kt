package com.example.testjetpack.ui.main.gitrepodetails

import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.models.git.db.GitRepositoryView
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import com.squareup.picasso.Picasso

class GitRepoDetailsFragmentVM(val picasso: Picasso) : BaseViewModel<GitRepoDetailsFragmentVMEventStateChange>() {
    var gitRepositoryView = MutableLiveData<GitRepositoryView>()
}

sealed class GitRepoDetailsFragmentVMEventStateChange : EventStateChange {
}