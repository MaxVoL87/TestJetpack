package com.example.testjetpack.ui.main.notifications

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentNotificationsBinding
import com.example.testjetpack.ui.base.BaseFragmentWithCallback
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.interpolateColor
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_notifications.*

import kotlin.reflect.KClass
import androidx.core.content.ContextCompat
import android.graphics.Point
import android.graphics.drawable.Drawable
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.testjetpack.ui.base.ICanBackPress
import com.example.testjetpack.utils.withNotNull


/**
 * A fragment representing a list of Notification Items.
 * Activities containing this fragment MUST implement the [INotificationsFragmentCallback] interface.
 */
class NotificationsFragment :
    BaseFragmentWithCallback<FragmentNotificationsBinding, NotificationsFragmentVM, INotificationsFragmentCallback>(),
    ICanBackPress {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bsView.progress
        withNotNull(activity?.windowManager?.defaultDisplay) {
            val point = Point()
            getSize(point)
            BottomSheetAnimator.init(
                bsView,
                point.x,
                ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                ContextCompat.getColor(requireContext(), R.color.colorBackgroundPrimary),
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_with_left_rounded_corner)!!
            )
        }
        BottomSheetBehavior.from(bsView).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            setBottomSheetCallback(BottomSheetAnimator)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getNotifications()
    }

    override fun onBackPressed(): Boolean {
        with(BottomSheetBehavior.from(bsView)) {
            return if (state != BottomSheetBehavior.STATE_COLLAPSED) {
                state = BottomSheetBehavior.STATE_COLLAPSED
                true
            } else false
        }
    }


// region VM renderers

    private val openNotificationRenderer: (Any) -> Unit = { event ->
        event as NotificationsFragmentVMEventStateChange.OpenNotifications
        callback?.openNotificationDetails(event.notification)
    }

    private val onNotificationsAddedRenderer: (Any) -> Unit = { event ->
        event as NotificationsFragmentVMEventStateChange.OnNotificationsAdded
        showAlert("Notifications added")
    }

    private val onNotificationsClearedRenderer: (Any) -> Unit = { event ->
        event as NotificationsFragmentVMEventStateChange.OnNotificationsCleared
        showAlert("Notifications cleared")
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        NotificationsFragmentVMEventStateChange.OpenNotifications::class to openNotificationRenderer,
        NotificationsFragmentVMEventStateChange.OnNotificationsAdded::class to onNotificationsAddedRenderer,
        NotificationsFragmentVMEventStateChange.OnNotificationsCleared::class to onNotificationsClearedRenderer
    )
// endregion VM renderers

    object BottomSheetAnimator : BottomSheetBehavior.BottomSheetCallback() {
        private var _initialyzed = false
        private var _bsWidth = 0
        private var _colorFrom = 0
        private var _colorTo = 0
        private lateinit var _shape: Drawable

        private var _isExpanded = false


        fun init(bsView: View, width: Int, colorFrom: Int, colorTo: Int, shape: Drawable) {
            bsView.translationX = width * 0.7F
            _bsWidth = width
            _colorFrom = colorFrom
            _colorTo = colorTo
            _shape = shape
            _initialyzed = true
        }

        override fun onSlide(bsView: View, slideOffset: Float) {
            if (!_initialyzed) return
            bsView.translationX = _bsWidth * (0.66F * (1 - slideOffset))
            bsView.backgroundTintList = ColorStateList.valueOf(interpolateColor(slideOffset, _colorFrom, _colorTo))

            if (bsView is MotionLayout) {
                bsView.progress = slideOffset
            }
            val isExpanded = slideOffset == 1.0F
            if (isExpanded != _isExpanded) {
                _isExpanded = isExpanded
                if (isExpanded) bsView.setBackgroundColor(_colorTo) else bsView.background = _shape
            }
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (!_initialyzed) return
        }
    }
}