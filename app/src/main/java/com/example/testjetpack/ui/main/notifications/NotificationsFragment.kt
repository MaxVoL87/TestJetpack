package com.example.testjetpack.ui.main.notifications

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentNotificationsBinding
import com.example.testjetpack.ui.base.BaseFragment
import com.example.testjetpack.ui.base.EventStateChange

import kotlin.reflect.KClass

/**
 * A fragment representing a list of Notification Items.
 * Activities containing this fragment MUST implement the [INotificationsFragmentCallback] interface.
 */
class NotificationsFragment : BaseFragment<FragmentNotificationsBinding, NotificationsFragmentVM>() {
    override val layoutId: Int = R.layout.fragment_notifications
    override val viewModelClass: KClass<NotificationsFragmentVM> = NotificationsFragmentVM::class
    override val observeLiveData: NotificationsFragmentVM.() -> Unit
        get() = {
        }


    private var _callback: INotificationsFragmentCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _callback = bindInterfaceOrThrow<INotificationsFragmentCallback>(parentFragment, context)
    }

    override fun onDetach() {
        _callback = null
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return view
    }

    override fun onStart() {
        super.onStart()
        viewModel.getNotifications()
    }

    override fun showProgress(text: String?) {
        _callback?.showProgress(text)
    }

    override fun hideProgress() {
        _callback?.hideProgress()
    }

    // region VM renderers

    private val openNotificationRenderer: (Any) -> Unit = { event ->
        event as NotificationsFragmentVMEventStateChange.OpenNotifications
        _callback?.openNotificationDetails(event.notification)
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        NotificationsFragmentVMEventStateChange.OpenNotifications::class to openNotificationRenderer
    )
    // endregion VM renderers
}
