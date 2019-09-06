package com.example.testjetpack.ui.base

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.google.gson.reflect.TypeToken
import kotlin.reflect.KClass

abstract class BaseFragmentWithCallback<B : ViewDataBinding, M : BaseViewModel<out EventStateChange>, C : IBaseCallback> :
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
        processActionWithDelay { callback?.showProgress(text) }
    }

    override fun hideProgress() {
        processActionWithDelay { callback?.hideProgress() }
    }

    private fun bindInterfaceOrThrow(clazz: KClass<C>, vararg objects: Any?): C {
        val typeToken = TypeToken.get(clazz.java) as TypeToken<C>
        return objects.find { it != null && typeToken.isAssignableFrom(it::class.java) }?.let { it as C }
            ?: throw NotImplementedInterfaceException(clazz.java)
    }

}