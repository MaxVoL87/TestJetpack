package com.example.testjetpack.ui.main.gps


import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat.getSystemService
import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentGpsBinding
import com.example.testjetpack.ui.base.BaseFragment
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.permissionsGranted
import com.example.testjetpack.utils.withNotNull
import kotlin.reflect.KClass



class GpsFragment : BaseFragment<FragmentGpsBinding, GpsFragmentVM>() {
    override val name: String = "GPS"
    override val viewModelClass: Class<GpsFragmentVM> = GpsFragmentVM::class.java
    override val layoutId: Int = R.layout.fragment_gps
    override val observeLiveData: GpsFragmentVM.() -> Unit
        get() = {
        }

    private var callback: IGpsFragmentCallback? = null

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

    private val isGpsOnlyItemId = 1
    private val isShowDiagnosticItemId = 2

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(Menu.NONE, isGpsOnlyItemId, Menu.NONE, "GPS Only")
        menu.add(Menu.NONE, isShowDiagnosticItemId, Menu.NONE, "Diagnostic")
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
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
        }

        return super.onOptionsItemSelected(item)
    }

    // region VM events renderer

    private val requestLocationUpdatesPermissions: (Any) -> Unit = { event ->
        event as GpsFragmentVMEventStateChange.RequestLocationUpdatesPermissions
        if(permissionsGranted(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.ACCESS_COARSE_LOCATION), true)){
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
