package com.example.testjetpack.ui.main.gitreposearch

import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.ui.base.BaseViewModel
import com.example.testjetpack.ui.base.EventStateChange
import javax.inject.Inject

class GitRepoSearchFragmentVM : BaseViewModel<GitRepoSearchFragmentVMEventStateChange>() {

    @Inject
    lateinit var repository: IDataRepository

    init {
        MainApplication.component.inject(this)
    }

}

sealed class GitRepoSearchFragmentVMEventStateChange : EventStateChange {
}