package com.example.testjetpack.ui.main.gps


import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentGpsBinding
import com.example.testjetpack.ui.base.BaseFragment
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.permissionsGranted
import com.google.android.gms.location.LocationServices
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
        binding.settingsClient = LocationServices.getSettingsClient(requireContext())
        binding.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        return view
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
