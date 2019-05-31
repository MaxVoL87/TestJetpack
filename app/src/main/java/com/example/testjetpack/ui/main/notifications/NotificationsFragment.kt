package com.example.testjetpack.ui.main.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentNotificationsBinding
import com.example.testjetpack.ui.base.BaseFragmentWithCallback
import com.example.testjetpack.ui.base.EventStateChange

import kotlin.reflect.KClass

/**
 * A fragment representing a list of Notification Items.
 * Activities containing this fragment MUST implement the [INotificationsFragmentCallback] interface.
 */
class NotificationsFragment :
    BaseFragmentWithCallback<FragmentNotificationsBinding, NotificationsFragmentVM, INotificationsFragmentCallback>() {
    override val layoutId: Int = R.layout.fragment_notifications
    override val viewModelClass: KClass<NotificationsFragmentVM> = NotificationsFragmentVM::class
    override val callbackClass: KClass<INotificationsFragmentCallback> = INotificationsFragmentCallback::class
    override val observeLiveData: NotificationsFragmentVM.() -> Unit
        get() = {
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return view
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
////        val mBottomSheetBehaviour = BottomSheetBehavior.from(bsView)
////        mBottomSheetBehaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
////            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//////                if(slideOffset >= 1){
//////                    ViewCompat.offsetLeftAndRight(bsView, 0)
//////                } else {
//////                    ViewCompat.offsetLeftAndRight(bsView, 1)
//////                }
////            }
////
////            override fun onStateChanged(bottomSheet: View, newState: Int) {
////            }
////
////        })
//        super.onViewCreated(view, savedInstanceState)
//    }

    override fun onStart() {
        super.onStart()
        viewModel.getNotifications()
    }

    // region VM renderers

    private val openNotificationRenderer: (Any) -> Unit = { event ->
        event as NotificationsFragmentVMEventStateChange.OpenNotifications
        callback?.openNotificationDetails(event.notification)
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        NotificationsFragmentVMEventStateChange.OpenNotifications::class to openNotificationRenderer
    )
    // endregion VM renderers
}