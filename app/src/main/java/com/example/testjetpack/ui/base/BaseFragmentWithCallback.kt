package com.example.testjetpack.ui.base

import android.content.Context
import androidx.databinding.ViewDataBinding
import kotlin.reflect.KClass

abstract class BaseFragmentWithCallback<B : ViewDataBinding, M : BaseViewModel<out EventStateChange>, C : ICallback> :
    BaseFragment<B, M>() {
    protected abstract val callbackClass: KClass<C>
    protected var callback: C? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow(clazz = callbackClass, objects = *arrayOf(parentFragment, context))
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }

    override fun showProgress(text: String?) {
        callback?.showProgress(text)
    }

    override fun hideProgress() {
        callback?.hideProgress()
    }

    @Suppress("UNCHECKED_CAST")
    private fun bindInterfaceOrThrow(clazz: KClass<C>, vararg objects: Any?): C {
        return objects.mapNotNull { it as? C }.firstOrNull()
            ?: throw NotImplementedInterfaceException(clazz.java)
    }

}