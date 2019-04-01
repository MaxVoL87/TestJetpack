package com.example.testjetpack.ui.main.gitreposearch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentGitreposearchBinding
import com.example.testjetpack.ui.base.BaseFragment
import com.example.testjetpack.ui.base.EventStateChange

import kotlin.reflect.KClass

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [IGitRepoSearchFragmentCallback] interface.
 */
class GitRepoSearchFragment : BaseFragment<FragmentGitreposearchBinding, GitRepoSearchFragmentVM>() {
    override val name: String = "Git Repo Search"
    override val viewModelClass: Class<GitRepoSearchFragmentVM> = GitRepoSearchFragmentVM::class.java
    override val layoutId: Int = R.layout.fragment_gitreposearch
    override val observeLiveData: GitRepoSearchFragmentVM.() -> Unit
        get() = {
        }

    private var callback: IGitRepoSearchFragmentCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<IGitRepoSearchFragmentCallback>(parentFragment, context)
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return view
    }

    // region VM events renderer

    private val openGitRepositoryRenderer: (Any) -> Unit = { event ->
        event as GitRepoSearchFragmentVMEventStateChange.OpenGitRepository
        callback?.openGitRepository(event.repo)
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        GitRepoSearchFragmentVMEventStateChange.OpenGitRepository::class to openGitRepositoryRenderer
    )
    // endregion VM events renderer

    companion object {
        @JvmStatic
        fun newInstance() = GitRepoSearchFragment()
    }

}
