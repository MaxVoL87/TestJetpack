package com.example.testjetpack.ui.main.notifications

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentNotificationListBinding
import com.example.testjetpack.ui.base.BaseFragment
import com.example.testjetpack.ui.base.EventStateChange

import kotlin.reflect.KClass

/**
 * A fragment representing a list of Notification Items.
 * Activities containing this fragment MUST implement the [INotificationFragmentCallback] interface.
 */
class NotificationFragment : BaseFragment<FragmentNotificationListBinding, NotificationFragmentVM>() {

    override val name: String = "Notifications"
    override val viewModelClass: Class<NotificationFragmentVM> = NotificationFragmentVM::class.java
    override val layoutId: Int = R.layout.fragment_notification_list
    override val observeLiveData: NotificationFragmentVM.() -> Unit
        get() = {
        }


    private var callback: INotificationFragmentCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<INotificationFragmentCallback>(parentFragment, context)
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

    override fun onStart() {
        super.onStart()
        viewModel.getNotifications()
    }

    // region VM renderers

    private val openNotificationRenderer: (Any) -> Unit = { event ->
        event as NotificationFragmentVMEventStateChange.OpenNotification
        callback?.openNotificationDetails(event.notification)
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        NotificationFragmentVMEventStateChange.OpenNotification::class to openNotificationRenderer
    )
    // endregion VM renderers

    companion object {
        @JvmStatic
        fun newInstance() = NotificationFragment()
    }
}
