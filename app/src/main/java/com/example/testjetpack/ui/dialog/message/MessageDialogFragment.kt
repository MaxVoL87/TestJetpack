package com.example.testjetpack.ui.dialog.message


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentMessageDialogBinding
import com.example.testjetpack.ui.base.BaseDialogFragment
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.bindInterfaceOrThrow
import com.example.testjetpack.utils.livedata.setIfNotTheSame
import com.example.testjetpack.utils.withNotNull
import kotlin.reflect.KClass

class MessageDialogFragment : BaseDialogFragment<FragmentMessageDialogBinding, MessageDialogFragmentVM>() {
    override val layoutId: Int = R.layout.fragment_message_dialog
    override val viewModelClass: KClass<MessageDialogFragmentVM> = MessageDialogFragmentVM::class
    override val observeLiveData: MessageDialogFragmentVM.() -> Unit
        get() = {
            isCancelable.observe(this@MessageDialogFragment, Observer { this@MessageDialogFragment.isCancelable = it })
        }

    private var _callback: IMessageDialogFragmentCallback? = null

    init {
        arguments = Bundle()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _callback = bindInterfaceOrThrow<IMessageDialogFragmentCallback>(parentFragment, context)
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

    fun setHeader(text: String?): MessageDialogFragment {
        arguments?.putString(EXTRA_HEADER, text)
        initState()
        return this
    }

    fun setText(text: String?): MessageDialogFragment {
        arguments?.putString(EXTRA_MESSAGE, text)
        initState()
        return this
    }

    fun setIsCancelable(isCancelable: Boolean): MessageDialogFragment {
        arguments?.putBoolean(EXTRA_IS_CANCELABLE, isCancelable)
        initState()
        return this
    }

    private fun initState(){
        withNotNull(arguments){
            try {
                viewModel.text.setIfNotTheSame(getString(EXTRA_HEADER))
                viewModel.text.setIfNotTheSame(getString(EXTRA_MESSAGE))
                viewModel.isCancelable.setIfNotTheSame(getBoolean(EXTRA_IS_CANCELABLE))
            } catch (ex: Throwable){/** VM not initialized **/}
        }
    }

    // region VM events renderer

    private val closeRenderer: (Any) -> Unit = { event ->
        event as MessageDialogFragmentVMEventStateChange.Close
        dismiss()
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        MessageDialogFragmentVMEventStateChange.Close::class to closeRenderer
    )
    // endregion VM events renderer

    companion object {
        private const val EXTRA_MESSAGE = "extra_message"
        private const val EXTRA_HEADER = "extra_header"
        private const val EXTRA_IS_CANCELABLE = "extra_is_Cancelable"
    }

}
