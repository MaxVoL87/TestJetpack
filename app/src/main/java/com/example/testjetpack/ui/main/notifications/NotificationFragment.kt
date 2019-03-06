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

//        // Set the adapter
//        if (view is RecyclerView) {
//            with(view) {
//                layoutManager = when {
//                    columnCount <= 1 -> LinearLayoutManager(context)
//                    else -> GridLayoutManager(context, columnCount)
//                }
//                adapter = MyNotificationRecyclerViewAdapter(DummyContent.ITEMS, listener)
//            }
//        }

        return view
    }


    // region VM renderers

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(

    )
    // endregion VM renderers

    companion object {
        @JvmStatic
        fun newInstance() = NotificationFragment()
    }
}
