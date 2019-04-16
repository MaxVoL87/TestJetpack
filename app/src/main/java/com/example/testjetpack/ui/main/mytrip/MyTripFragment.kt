package com.example.testjetpack.ui.main.mytrip


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentMyTripBinding
import com.example.testjetpack.ui.base.BaseFragment
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.*
import com.google.android.gms.maps.*
import kotlin.reflect.KClass
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


/**
 * Activities that contain this fragment must implement the
 * [IMyTripFragmentCallback] interface to handle interaction events.
 *
 */
class MyTripFragment : BaseFragment<FragmentMyTripBinding, MyTripFragmentVM>(), OnMapReadyCallback {
    override val layoutId: Int = R.layout.fragment_my_trip
    override val viewModelClass: KClass<MyTripFragmentVM> = MyTripFragmentVM::class
    override val observeLiveData: MyTripFragmentVM.() -> Unit
        get() = {
        }

    private var _callback: IMyTripFragmentCallback? = null
    private var _mapFragment: SupportMapFragment = SupportMapFragment.newInstance(
        GoogleMapOptions()
            .mapType(GoogleMap.MAP_TYPE_NORMAL)
            .liteMode(false)
            .compassEnabled(true)
            .ambientEnabled(true)
            .mapToolbarEnabled(false) // navigate to maps
            .zoomControlsEnabled(true)
            .zoomGesturesEnabled(true) // custom
    )
    private var _map: GoogleMap? = null
    private val _locationPermitsReqCode = 121

    override fun onMapReady(map: GoogleMap?) {
        _map = map
        _map?.apply {
            withNotNull(context?.getActionBarHeightPx(), context?.convertDpToPixels(10)) { abHeight, correction ->
                setPadding(0, abHeight - correction, 0, 0)
            }
            uiSettings.isIndoorLevelPickerEnabled = true
            uiSettings.isRotateGesturesEnabled = true
        }
        initLocationState()


        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        _map?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        _map?.moveCamera(CameraUpdateFactory.newLatLng(sydney))

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _callback = bindInterfaceOrThrow<IMyTripFragmentCallback>(parentFragment, context)
        _mapFragment.onAttach(context)
    }

    override fun onDetach() {
        _callback = null
        super.onDetach()
    }

    override fun onInflate(activity: Activity, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(activity, attrs, savedInstanceState)
        _mapFragment.onInflate(activity, attrs, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _mapFragment.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        _mapFragment.onCreateView(inflater, container, savedInstanceState)
        fragmentManager?.beginTransaction()?.add(R.id.fMap, _mapFragment)?.commit()

        binding.viewModel = viewModel
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        _mapFragment.onActivityCreated(savedInstanceState)
        _mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        _mapFragment.onStart()
    }

    override fun onResume() {
        super.onResume()
        _mapFragment.onResume()
    }

    override fun onPause() {
        _mapFragment.onPause()
        super.onPause()
    }

    override fun onStop() {
        _mapFragment.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        fragmentManager?.beginTransaction()?.remove(_mapFragment)?.commit()
        _mapFragment.onDestroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        _mapFragment.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        _mapFragment.onLowMemory()
        super.onLowMemory()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        _mapFragment.onSaveInstanceState(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == _locationPermitsReqCode && resultCode == Activity.RESULT_OK) {
            initLocationState()
        }
    }

    @SuppressLint("MissingPermission")
    private fun initLocationState() {
        withNotNull(_map) {
            if (permissionsGranted(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), true, _locationPermitsReqCode
                )
            ) {
                uiSettings.isMyLocationButtonEnabled = true
                isMyLocationEnabled = true
            }
        }
    }

    // region VM events renderer

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
    )
    // endregion VM events renderer

}
