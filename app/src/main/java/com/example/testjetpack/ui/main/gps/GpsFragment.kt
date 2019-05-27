package com.example.testjetpack.ui.main.gps


import android.Manifest
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.core.content.ContextCompat.getSystemService
import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentGpsBinding
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.permissionsGranted
import com.example.testjetpack.utils.withNotNull
import androidx.lifecycle.Observer
import com.example.testjetpack.ui.base.BaseFragmentWithCallback
import com.example.testjetpack.utils.binding.onClick
import kotlinx.android.synthetic.main.backdrop_gps.*
import kotlin.reflect.KClass


class GpsFragment : BaseFragmentWithCallback<FragmentGpsBinding, GpsFragmentVM, IGpsFragmentCallback>() {
    override val layoutId: Int = R.layout.fragment_gps
    override val viewModelClass: KClass<GpsFragmentVM> = GpsFragmentVM::class
    override val callbackClass: KClass<IGpsFragmentCallback> = IGpsFragmentCallback::class
    override val observeLiveData: GpsFragmentVM.() -> Unit
        get() = {
            isGPSOnly.observe(this@GpsFragment, Observer<Boolean> {
                withNotNull(it) {
                    chbIsGpsOnly.isChecked = this
                    invalidateOptionsMenu()
                }
            })
            isLocationListenerStarted.observe(this@GpsFragment, Observer<Boolean> {
                withNotNull(it) {
                    chbIsGpsOnly.isEnabled = !this
                    invalidateOptionsMenu()
                }
            })
            isNeedToShowDiagnostic.observe(this@GpsFragment, Observer<Boolean> {
                withNotNull(it) {
                    chbIsShowDiagnostic.isChecked = this
                    invalidateOptionsMenu()
                }
            })
        }

    private val _isGpsOnlyItemId = 1
    private val _isShowDiagnosticItemId = 2
    private val _menuItems = mutableMapOf<Int, MenuItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Binding not working on <merge/>
        chbIsGpsOnly.setOnCheckedChangeListener { _, isChecked -> viewModel.isGPSOnly.value = isChecked }
        chbIsShowDiagnostic.setOnCheckedChangeListener { _, isChecked -> viewModel.isNeedToShowDiagnostic.value = isChecked }
        bClearLocations.onClick(Runnable { viewModel.clearDBData { showAlert("DB Cleared") } })
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val locationManager = getSystemService(requireContext(), LocationManager::class.java) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showAlert("GPS Not Enabled")
        }
    }

    // region options menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        _menuItems[_isGpsOnlyItemId] =
            menu.add(Menu.NONE, _isGpsOnlyItemId, Menu.NONE, chbIsGpsOnly.text).apply {
                setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM or MenuItem.SHOW_AS_ACTION_WITH_TEXT)
                isCheckable = true
            }
        _menuItems[_isShowDiagnosticItemId] =
            menu.add(Menu.NONE, _isShowDiagnosticItemId, Menu.NONE, chbIsShowDiagnostic.text).apply {
                setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM or MenuItem.SHOW_AS_ACTION_WITH_TEXT)
                isCheckable = true
            }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        _menuItems[_isGpsOnlyItemId]?.apply {
            setChecked(this, viewModel.isGPSOnly.value == true)
            isEnabled = viewModel.isLocationListenerStarted.value != true
        }
        _menuItems[_isShowDiagnosticItemId]?.apply {
            setChecked(this, viewModel.isNeedToShowDiagnostic.value == true)
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            _isGpsOnlyItemId -> {
                withNotNull(viewModel.isGPSOnly.value) {
                    viewModel.isGPSOnly.value = !this
                }
                return true
            }
            _isShowDiagnosticItemId -> {
                withNotNull(viewModel.isNeedToShowDiagnostic.value) {
                    viewModel.isNeedToShowDiagnostic.value = !this
                }
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setChecked(item: MenuItem?, checked: Boolean) {
        withNotNull(item) {
            if (isChecked != checked) isChecked = checked
            title = SpannableString(title).also {
                it.setSpan(ForegroundColorSpan(if (checked) Color.BLACK else Color.LTGRAY), 0, it.length, 0)
            }
        }
    }

// endregion options menu

    // region VM events renderer

    private val requestLocationUpdatesPermissions: (Any) -> Unit = { event ->
        event as GpsFragmentVMEventStateChange.RequestLocationUpdatesPermissions
        if (permissionsGranted(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                true
            )
        ) {
            viewModel.onPermissionSuccess()
        } else {
            showAlert("Permissions Not Granted!")
        }
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        GpsFragmentVMEventStateChange.RequestLocationUpdatesPermissions::class to requestLocationUpdatesPermissions
    )
    // endregion VM events renderer
}
