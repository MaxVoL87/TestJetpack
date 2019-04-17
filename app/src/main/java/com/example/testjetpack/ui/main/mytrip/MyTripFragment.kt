package com.example.testjetpack.ui.main.mytrip


import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentMyTripBinding
import com.example.testjetpack.models.own.Trip
import com.example.testjetpack.ui.base.BaseFragment
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import java.util.*
import kotlin.reflect.KClass


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
            trip.observe(this@MyTripFragment, Observer<Trip>(::setTrip))
            position.observe(this@MyTripFragment, Observer<LatLng>(::setPosition))
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
    private var _positionPropeller: PositionMarkerPropeller? = null

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
        initPositionPropeller()
    }

    // region Lifecycle
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
        viewModel.getTrip()
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
        _positionPropeller?.clear()
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

    // endregion Lifecycle

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

    private fun setTrip(trip: Trip) {
        withNotNull(_map) {
            clear()

            if (trip.locations.size < 2) {
                showAlert("Empty Trip")
                return
            }
            addMarker(getPointMarkerOpt().position(trip.locations.first()).title("A"))
            addMarker(getPointMarkerOpt().position(trip.locations.last()).title("B"))
            val options = PolylineOptions().apply {
                zIndex(TRIP_POLY_ZIND)
                color(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                width(TRIP_POLY_WIDTH)
                visible(true)
                addAll(trip.locations)
            }
            addPolyline(options)

            moveCamera(*trip.locations.toTypedArray())
        }
    }

    private fun setPosition(position: LatLng) {
        _positionPropeller?.setPosition(position)
    }

    private fun moveCamera(vararg markers: LatLng) {
        val maxLat = markers.map { it.latitude }.max()
        val maxLong = markers.map { it.longitude }.max()
        val minLat = markers.map { it.latitude }.min()
        val minLong = markers.map { it.longitude }.min()

        withNotNull(_map) {
            moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                    LatLngBounds(
                        LatLng(minLat!!, minLong!!),
                        LatLng(maxLat!!, maxLong!!)
                    ),
                    requireContext().convertDpToPixels(DEF_MAP_CAMERA_PADDING) //todo: calculate position
                )
            )
        }
    }

    private fun initPositionPropeller() {
        withNotNull(_map) {
            _positionPropeller = PositionMarkerPropeller(
                this,
                getPositionMarkerOpt().title("Position")
            )
        }
    }

    private fun getRotationAngle(from: LatLng, to: LatLng): Float {
        val a = to.latitude - from.latitude
        val b = to.longitude - from.longitude
        var angle = Math.toDegrees(Math.atan(b / a)).toFloat()
        if (a < 0) angle += 180
        return angle + DEF_POS_IMAGE_TILT
    }

    private fun getPointMarkerOpt(): MarkerOptions {
        return MarkerOptions().zIndex(PIN_MARKER_ZIND)
    }

    private fun getPositionMarkerOpt(): MarkerOptions {
        return MarkerOptions()
            .zIndex(POSITION_MARKER_ZIND)
            .icon(requireContext().bitmapDescriptorFromVector(R.drawable.ic_navigation_black_32dp))
            .anchor(0.5F, 0.5F)
    }
    // region VM events renderer

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
    )
    // endregion VM events renderer

    companion object {
        private const val DEF_POS_IMAGE_TILT = 0F
        private const val MOVING_SPEED_MILLIS = 5000L
        private const val DEF_MAP_CAMERA_PADDING = 50

        private const val TRIP_POLY_WIDTH = 10F
        private const val TRIP_POLY_ZIND = 80F

        private const val POSITION_MARKER_ZIND = 90F
        private const val PIN_MARKER_ZIND = 100F
    }

    inner class PositionMarkerPropeller(
        private val map: GoogleMap,
        private val markerOptions: MarkerOptions
    ) {
        private var marker: Marker? = null
        private val valueAnimator: ValueAnimator = ValueAnimator.ofFloat(0F, 1F).apply {
            interpolator = LinearInterpolator()
        }
        private var animatorUpdateListener: ValueAnimator.AnimatorUpdateListener? = null
        private var isPaused: Boolean = false
        private var curPosition: LatLng? = null
        private var lastPositionTime: Long = 0

        fun setPosition(position: LatLng) {
            when {
                isMarkerInit() -> {
                    markerOptions.position(position)
                    marker = map.addMarker(markerOptions)
                }
                isPaused -> {
                    marker = map.addMarker(markerOptions)
                    setToPoint(marker!!, position)
                    isPaused = false
                }
                curPosition != null && curPosition != marker?.position -> {
                    setToPoint(marker!!, curPosition!!)
                    animateToPoint(marker!!, position)
                }
                else -> animateToPoint(marker!!, position)
            }
            curPosition = position
        }

        fun clear() {
            valueAnimator.cancel()
            isPaused = true
        }

        private fun isMarkerInit(): Boolean {
            return marker == null
        }

        private fun animateToPoint(marker: Marker, to: LatLng) {
            moveFromTo(marker, marker.position, to)
        }

        private fun setToPoint(marker: Marker, to: LatLng) {
            marker.position = to
        }

        private fun moveFromTo(marker: Marker, from: LatLng, to: LatLng) {
            valueAnimator.cancel()
            animatorUpdateListener?.let { valueAnimator.removeUpdateListener(it) }

            val angle = getRotationAngle(from, to)
            animatorUpdateListener = ValueAnimator.AnimatorUpdateListener { v ->
                val value = v.animatedFraction
                val lng = from.longitude * (1 - value) + to.longitude * value
                val lat = from.latitude * (1 - value) + to.latitude * value
                val intermediatePoint = LatLng(lat, lng)
                marker.position = intermediatePoint
                marker.rotation = angle
            }
            val time = Calendar.getInstance().timeInMillis
            valueAnimator.duration = if(lastPositionTime in 1 until time) time - lastPositionTime else MOVING_SPEED_MILLIS
            valueAnimator.addUpdateListener(animatorUpdateListener)
            valueAnimator.start()
            lastPositionTime = time
        }
    }
}
