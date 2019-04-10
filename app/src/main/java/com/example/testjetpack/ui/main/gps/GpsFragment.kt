package com.example.testjetpack.ui.main.gps


import android.Manifest
import android.content.Context
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.core.content.ContextCompat.getSystemService
import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentGpsBinding
import com.example.testjetpack.ui.base.BaseFragment
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.permissionsGranted
import com.example.testjetpack.utils.withNotNull
import androidx.lifecycle.Observer
import org.koin.androidx.scope.currentScope
import kotlin.reflect.KClass


class GpsFragment : BaseFragment<FragmentGpsBinding, GpsFragmentVM>() {
    override val name: String = "GPS"
    override val viewModelClass: KClass<GpsFragmentVM> = GpsFragmentVM::class
    override val layoutId: Int = R.layout.fragment_gps
    override val observeLiveData: GpsFragmentVM.() -> Unit
        get() = {
            isGPSOnly.observe(this@GpsFragment, Observer<Boolean> {
                setChecked(menuItems[isGpsOnlyItemId], it)
            })
            isLocationListenerStarted.observe(this@GpsFragment, Observer<Boolean> {
                menuItems[isGpsOnlyItemId]?.isEnabled != it
            })
            isNeedToShowDiagnostic.observe(this@GpsFragment, Observer<Boolean> {
                setChecked(menuItems[isShowDiagnosticItemId], it)
            })
        }

    private var callback: IGpsFragmentCallback? = null

    private val isGpsOnlyItemId = 1
    private val isShowDiagnosticItemId = 2
    private val clearDBLocationsDataItemId = 3
    private val menuItems = mutableMapOf<Int, MenuItem>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<IGpsFragmentCallback>(parentFragment, context)
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
        val locationManager = getSystemService(requireContext(), LocationManager::class.java) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showAlert("GPS Not Enabled")
        }
    }

    // region options menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menuItems[isGpsOnlyItemId] = menu.add(Menu.NONE, isGpsOnlyItemId, Menu.NONE, "GPS Only").apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM or MenuItem.SHOW_AS_ACTION_WITH_TEXT)
            isCheckable = true
        }
        menuItems[isShowDiagnosticItemId] = menu.add(Menu.NONE, isShowDiagnosticItemId, Menu.NONE, "Diagnostic").apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM or MenuItem.SHOW_AS_ACTION_WITH_TEXT)
            isCheckable = true
        }
        menuItems[clearDBLocationsDataItemId] = menu.add(Menu.NONE, clearDBLocationsDataItemId, Menu.NONE, "Clear locations")
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menuItems[isGpsOnlyItemId]?.apply {
            setChecked(this, viewModel.isGPSOnly.value == true)
            isEnabled = viewModel.isLocationListenerStarted.value != true
        }
        menuItems[isShowDiagnosticItemId]?.apply {
            setChecked(this, viewModel.isNeedToShowDiagnostic.value == true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            isGpsOnlyItemId -> {
                withNotNull(viewModel.isGPSOnly.value) {
                    viewModel.isGPSOnly.value = !this
                }
                return true
            }
            isShowDiagnosticItemId -> {
                withNotNull(viewModel.isNeedToShowDiagnostic.value) {
                    viewModel.isNeedToShowDiagnostic.value = !this
                }
                return true
            }
            clearDBLocationsDataItemId -> {
                viewModel.clearDBData()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setChecked(item: MenuItem?, checked: Boolean) {
        withNotNull(item) {
            isChecked = checked
            title = SpannableString(title).also {
                it.setSpan(ForegroundColorSpan(if (checked) Color.RED else Color.BLACK), 0, it.length, 0)
            }
        }
    }
// endregion options menu

    // region VM events renderer

    private val requestLocationUpdatesPermissions: (Any) -> Unit = { event ->
        event as GpsFragmentVMEventStateChange.RequestLocationUpdatesPermissions
        if (permissionsGranted(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), true
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

    companion object {
        @JvmStatic
        fun newInstance() = GpsFragment()
    }
}
