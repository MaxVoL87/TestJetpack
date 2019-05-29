package com.example.testjetpack.ui.main.gitreposearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentGitreposearchBinding
import com.example.testjetpack.ui.base.BaseFragmentWithCallback
import com.example.testjetpack.ui.base.EventStateChange

import kotlin.reflect.KClass

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [IGitRepoSearchFragmentCallback] interface.
 */
class GitRepoSearchFragment : BaseFragmentWithCallback<FragmentGitreposearchBinding, GitRepoSearchFragmentVM, IGitRepoSearchFragmentCallback>() {
    override val layoutId: Int = R.layout.fragment_gitreposearch
    override val viewModelClass: KClass<GitRepoSearchFragmentVM> = GitRepoSearchFragmentVM::class
    override val callbackClass: KClass<IGitRepoSearchFragmentCallback> = IGitRepoSearchFragmentCallback::class
    override val observeLiveData: GitRepoSearchFragmentVM.() -> Unit
        get() = {
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return view
    }

    override fun showProgress(text: String?) {
        //do nothing
    }

    override fun hideProgress() {
        //do nothing
    }

    // region VM events renderer

    private val openGitRepositoryRenderer: (Any) -> Unit = { event ->
        event as GitRepoSearchFragmentVMEventStateChange.OpenGitRepository
        callback?.openGitRepository(event.repoUrl)
    }

    private val showGitRepositoryRenderer: (Any) -> Unit = { event ->
        event as GitRepoSearchFragmentVMEventStateChange.ShowGitRepository
        callback?.showGitRepository(event.gitRepository, event.fragmentNavigatorExtras)
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        GitRepoSearchFragmentVMEventStateChange.OpenGitRepository::class to openGitRepositoryRenderer,
        GitRepoSearchFragmentVMEventStateChange.ShowGitRepository::class to showGitRepositoryRenderer
    )
    // endregion VM events renderer

}
