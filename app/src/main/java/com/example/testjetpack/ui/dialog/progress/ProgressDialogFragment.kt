package com.example.testjetpack.ui.dialog.progress

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentProgressDialogBinding
import com.example.testjetpack.ui.base.BaseDialogFragment
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.bindInterfaceOrThrow
import com.example.testjetpack.utils.livedata.setIfNotTheSame
import com.example.testjetpack.utils.withNotNull
import kotlin.reflect.KClass

class ProgressDialogFragment : BaseDialogFragment<FragmentProgressDialogBinding, ProgressDialogFragmentVM>() {
    override val layoutId: Int = R.layout.fragment_progress_dialog
    override val viewModelClass: KClass<ProgressDialogFragmentVM> = ProgressDialogFragmentVM::class
    override val observeLiveData: ProgressDialogFragmentVM.() -> Unit
        get() = {
            isCancelable.observe(this@ProgressDialogFragment, Observer { this@ProgressDialogFragment.isCancelable = it })
            cancel.observe(this@ProgressDialogFragment, Observer { if (it) this@ProgressDialogFragment.dismiss() })
        }

    private var _callback: IProgressDialogFragmentCallback? = null

    init {
        arguments = Bundle()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _callback = bindInterfaceOrThrow<IProgressDialogFragmentCallback>(parentFragment, context)
    }

    override fun onDetach() {
        _callback = null
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initState()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return view
    }

    fun setText(text: String?): ProgressDialogFragment {
        arguments?.putString(EXTRA_MESSAGE, text)
        initState()
        return this
    }

    fun setPBVisible(isVisible: Boolean): ProgressDialogFragment {
        arguments?.putBoolean(EXTRA_IS_PB_VISIBLE, isVisible)
        initState()
        return this
    }

    fun setIsCancelable(isCancelable: Boolean): ProgressDialogFragment {
        arguments?.putBoolean(EXTRA_IS_CANCELABLE, isCancelable)
        initState()
        return this
    }

    fun cancel() {
        viewModel.cancel.value = true
    }

    private fun initState(){
        withNotNull(arguments){
            try {
                viewModel.text.setIfNotTheSame(getString(EXTRA_MESSAGE))
                viewModel.isProgressBarVisible.setIfNotTheSame(getBoolean(EXTRA_IS_PB_VISIBLE))
                viewModel.isCancelable.setIfNotTheSame(getBoolean(EXTRA_IS_CANCELABLE))
            } catch (ex: Throwable){/** VM not initialized **/}
        }
    }

    // region VM events renderer

    private val closeRenderer: (Any) -> Unit = { event ->
        event as ProgressDialogFragmentVMEventStateChange.Close
        dismiss()
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        ProgressDialogFragmentVMEventStateChange.Close::class to closeRenderer
    )
    // endregion VM events renderer

    companion object {
        private const val EXTRA_MESSAGE = "extra_message"
        private const val EXTRA_IS_PB_VISIBLE = "extra_is_pb_visible"
        private const val EXTRA_IS_CANCELABLE = "extra_is_Cancelable"
    }
}
