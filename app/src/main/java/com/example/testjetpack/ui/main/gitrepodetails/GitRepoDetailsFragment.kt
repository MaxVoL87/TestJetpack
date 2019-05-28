package com.example.testjetpack.ui.main.gitrepodetails


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.TransitionInflater

import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentGitrepodetailsBinding
import com.example.testjetpack.ui.base.BaseFragmentWithCallback
import com.example.testjetpack.ui.base.EventStateChange
import kotlin.reflect.KClass

class GitRepoDetailsFragment :
    BaseFragmentWithCallback<FragmentGitrepodetailsBinding, GitRepoDetailsFragmentVM, IGitRepoDetailsFragmentCallback>() {
    override val layoutId: Int = R.layout.fragment_gitrepodetails
    override val viewModelClass: KClass<GitRepoDetailsFragmentVM> = GitRepoDetailsFragmentVM::class
    override val callbackClass: KClass<IGitRepoDetailsFragmentCallback> = IGitRepoDetailsFragmentCallback::class
    override val observeLiveData: GitRepoDetailsFragmentVM.() -> Unit
        get() = {
        }

    private var transitionName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.transition_default).apply {
            duration = 350
            startDelay = 200
        }
        arguments?.let {
            val safeArgs = GitRepoDetailsFragmentArgs.fromBundle(it)
            viewModel.gitRepositoryView.value = safeArgs.gitRepositoryView
            transitionName = safeArgs.transitionName
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.findViewById<View>(R.id.ivUserAvatar)?.transitionName = transitionName
        binding.viewModel = viewModel
        return view
    }


    // region VM events renderer

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
    )
    // endregion VM events renderer
}
