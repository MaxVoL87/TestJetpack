package com.example.testjetpack.ui.main.gitrepodetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.testjetpack.MainApplicationContract.DEFAULT_UI_DELAY

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

    private val navArgs by navArgs<GitRepoDetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.transition_default)
            .apply {
                duration = DEFAULT_UI_DELAY
            }

        viewModel.gitRepoDetailsFragmentArgs.value = navArgs
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return view
    }

    // region VM events renderer

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
    )
    // endregion VM events renderer
}
