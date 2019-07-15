package com.example.testjetpack.ui.main.mytrip


import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentMyTripBinding
import com.example.testjetpack.models.own.Location
import com.example.testjetpack.models.own.Trip
import com.example.testjetpack.ui.base.BaseFragmentWithCallback
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.utils.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import java.util.*
import kotlin.math.atan
import kotlin.reflect.KClass


/**
 * Activities that contain this fragment must implement the
 * [IMyTripFragmentCallback] interface to handle interaction events.
 *
 */
class MyTripFragment : BaseFragmentWithCallback<FragmentMyTripBinding, MyTripFragmentVM, IMyTripFragmentCallback>(),
    OnMapReadyCallback {
    override val layoutId: Int = R.layout.fragment_my_trip
    override val viewModelClass: KClass<MyTripFragmentVM> = MyTripFragmentVM::class
    override val callbackClass: KClass<IMyTripFragmentCallback> = IMyTripFragmentCallback::class
    override val observeLiveData: MyTripFragmentVM.() -> Unit
        get() = {
            trip.observe(this@MyTripFragment, Observer<Trip>(::setTrip))
            position.observe(this@MyTripFragment, Observer<Location>(::setPosition))
        }

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        childFragmentManager.beginTransaction().add(R.id.fMap, _mapFragment).commit()
        binding.viewModel = viewModel
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        _mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getTrip()
    }

    override fun onStop() {
        super.onStop()
        _positionPropeller?.stop()
    }

    override fun onDestroyView() {
        childFragmentManager.beginTransaction().remove(_mapFragment).commitAllowingStateLoss()
        super.onDestroyView()
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
            clearMap()

            if (trip.locations.size < 2) {
                showAlert("Empty Trip")
                return
            }
            val latLngs = trip.locations.map { it.latLng }
            _markers.add(addMarker(getPointMarkerOpt().position(latLngs.first()).title("A")))
            _markers.add(addMarker(getPointMarkerOpt().position(latLngs.last()).title("B")))
            val options = PolylineOptions().apply {
                zIndex(TRIP_POLY_ZIND)
                color(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                width(TRIP_POLY_WIDTH)
                visible(true)
                addAll(latLngs)
            }
            _polylines.add(addPolyline(options))

            moveCamera(*latLngs.toTypedArray())
        }
    }

    private fun setPosition(position: Location) {
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
        var angle = Math.toDegrees(atan(b / a)).toFloat()
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

    private fun clearMap() {
        _map?.clear()
        _markers.clear()
        _polylines.clear()
    }

    private val _markers = ObservableNoDuplicateList<Marker>()
    private val _polylines = ObservableNoDuplicateList<Polyline>()

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
        private val _map: GoogleMap,
        private val _markerOptions: MarkerOptions
    ) {
        private var _marker: Marker? = null
        private var _animatorUpdateListener = object : ValueAnimator.AnimatorUpdateListener {
            private var from: Location? = null
            private var to: Location? = null

            @Synchronized
            fun setTo(location: Location) {
                from = to
                to = location
            }

            override fun onAnimationUpdate(v: ValueAnimator) {
                withNotNull(_marker, from, to) { m, f, t ->
                    val fromPos = f.latLng
                    val toPos = t.latLng

                    val value = v.animatedFraction
                    val lng = fromPos.longitude * (1 - value) + toPos.longitude * value
                    val lat = fromPos.latitude * (1 - value) + toPos.latitude * value
                    m.position = LatLng(lat, lng)
                }
            }
        }
        private val _valueAnimator: ValueAnimator = ValueAnimator.ofFloat(0F, 1F).apply {
            interpolator = LinearInterpolator()
            addUpdateListener(_animatorUpdateListener)
        }
        private var _isStopped: Boolean = false
        private var _lastPositionTime: Long = 0

        fun setPosition(location: Location) {
            _animatorUpdateListener.setTo(location)

            if (isMarkerInit()) {
                initMarker(location)
            }
            else {
                if(_isStopped) moveMarkerImmediately(location)
                else moveMarker(location)
            }

            _isStopped = false
        }

        fun stop() {
            _valueAnimator.cancel()
            _isStopped = true
        }

        private fun isMarkerInit(): Boolean {
            with(_marker) {
                return this == null || !_markers.contains(this)
            }
        }

        private fun initMarker(location: Location) {
            _markerOptions.position(location.latLng)
            _marker = _map.addMarker(_markerOptions)
            _markers.add(_marker!!)
        }

        private fun moveMarker(to: Location) = with(_marker) {
            if (this == null || position == to.latLng) return
            _valueAnimator.cancel()

            val time = Calendar.getInstance().timeInMillis
            _valueAnimator.duration = if (_lastPositionTime in 1 until time) time - _lastPositionTime else MOVING_SPEED_MILLIS
            _lastPositionTime = time

            rotation = getRotationAngle(position, to.latLng)

            _valueAnimator.start()
        }

        private fun moveMarkerImmediately(to: Location) = with(_marker) {
            if (this == null || position == to.latLng) return
            _valueAnimator.cancel()

            _lastPositionTime = Calendar.getInstance().timeInMillis
            rotation = getRotationAngle(position, to.latLng)

            position = to.latLng
        }

    }
}
