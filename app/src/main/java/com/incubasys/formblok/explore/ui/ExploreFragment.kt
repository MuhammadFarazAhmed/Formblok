package com.incubasys.formblok.explore.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources.NotFoundException
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.BillingFlowParams
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.SphericalUtil
import com.google.maps.android.clustering.ClusterManager
import com.incubasys.formblok.R
import com.incubasys.formblok.common.data.remote.ApiStatus.*
import com.incubasys.formblok.common.extenstions.hideKeyboard
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.common.ui.LocationSetupDialogFragment
import com.incubasys.formblok.custom.CustomRenderer
import com.incubasys.formblok.databinding.ExploreFragmentBinding
import com.incubasys.formblok.explore.adapter.PlacesAutoCompleteAdapter
import com.incubasys.formblok.explore.callback.ExploreFragmentCallback
import com.incubasys.formblok.explore.data.model.AddressMapItem
import com.incubasys.formblok.explore.viewmodel.ExploreViewModel
import com.incubasys.formblok.util.Constants.CAMERA_ZOOM_CURRENT_LOC
import kotlinx.android.synthetic.main.explore_fragment.*
import javax.inject.Inject
import kotlin.math.floor


class ExploreFragment : BaseFragment(), OnMapReadyCallback,
    PlacesAutoCompleteAdapter.ClickListener {


    override fun click(place: Place) {
        place.latLng?.let {
            etExploreFragmentSearchBox.setText(place.name)
            onLocationChanged(it)
        }
        rvPredictions.visibility = View.GONE
    }

    private lateinit var adapter: PlacesAutoCompleteAdapter
    private lateinit var placesClient: PlacesClient
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ExploreFragmentBinding
    private lateinit var callback: ExploreFragmentCallback
    private lateinit var mClusterManager: ClusterManager<AddressMapItem>
    private var currentMapCenter: LatLng? = null

    companion object {
        fun newInstance(projectId: Int? = -1) = ExploreFragment().apply {
            arguments = bundleOf("projectId" to projectId)
        }
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    val viewModel: ExploreViewModel by lazy {
        ViewModelProviders.of(this, factory)
            .get(ExploreViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ExploreFragmentCallback) {
            callback = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.selectedProjectId = it.getInt("projectId",-1)
        }
        activity?.let {
            Places.initialize(activity?.applicationContext!!, getString(R.string.google_maps_key))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.explore_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (Places.isInitialized()) {
            placesClient = Places.createClient(context!!)
            context?.let {
                adapter = PlacesAutoCompleteAdapter(context!!, placesClient)
            }
        }

        callback.requestLocation()

        context?.let {
            initPlaceAutoComplete(context!!)
        }

        ivCompass.setOnClickListener {
            viewModel.currentLatLng.value?.let {
                moveMapToLocation(it)
            } ?: callback.requestLocation()
        }

        viewModel.addressListResponse.observe(this, Observer {
            when (it.status) {
                LOADING -> showProgress()
                SUCCESS -> {
                    dismissProgress()
                    mClusterManager.clearItems()
                    mClusterManager.addItems(it.data)
                    mClusterManager.cluster()
                }
                ERROR -> {
                    dismissProgress()
                }
                EMPTY -> {
                }
                COMPLETED -> {
                }
            }
        })
    }

    private fun initPlaceAutoComplete(context: Context) {

        etExploreFragmentSearchBox.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
            }
            true
        }

        etExploreFragmentSearchBox.doAfterTextChanged {
            if (it.toString() != "") {
                adapter.filter.filter(it.toString())
                if (rvPredictions.visibility == View.GONE) {
                    rvPredictions.visibility = View.VISIBLE
                }
            } else {
                if (rvPredictions.visibility == View.VISIBLE) {
                    rvPredictions.visibility = View.GONE
                }
            }
        }

        rvPredictions.layoutManager = LinearLayoutManager(context)
        adapter.setClickListener(this)
        rvPredictions.adapter = adapter
        adapter.notifyDataSetChanged()
        TransitionManager.beginDelayedTransition(rvPredictions)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        /**
         * init Map Ui Settings
         */
        with(mMap.uiSettings) {
            isMyLocationButtonEnabled = false
            isCompassEnabled = false
            isMapToolbarEnabled = false
            isRotateGesturesEnabled = false
            isTiltGesturesEnabled = false
        }

        /**
         * Adding style to Map
         */
        try {
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    activity, R.raw.map_style
                )
            )
            if (!success) {
                Log.e("Map", "Style parsing failed.")
            }
        } catch (e: NotFoundException) {
            Log.e("Map", "Can't find style. Error: ", e)
        }

        /**
         * Move Camera To Specific position and set clustering on Map
         */
        /*mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(51.503186, -0.126446), 10f)
        )*/
        mClusterManager = ClusterManager(activity, mMap)
        context?.let {
            mClusterManager.renderer = CustomRenderer(it, mMap, mClusterManager)
        }
        //mMap.setOnCameraIdleListener(mClusterManager)
        mMap.setOnMarkerClickListener(mClusterManager)
        mClusterManager.setOnClusterItemClickListener { addressMapItem ->
            callback.onAddressClicked(
                viewModel.selectedProjectId,
                addressMapItem.address.id,
                LatLng(addressMapItem.address.lat, addressMapItem.address.lng)
            )
            return@setOnClusterItemClickListener true
        }

        mMap.setOnCameraIdleListener {
            val previousMapCenter = currentMapCenter
            currentMapCenter = mMap.cameraPosition.target
            if (previousMapCenter == null || SphericalUtil.computeDistanceBetween(
                    currentMapCenter,
                    previousMapCenter
                ) > 5000
            ) {
                if (viewModel.currentLatLng.value != null) {
                    currentMapCenter?.let { viewModel.fetchAddresses(it) }
                }
            }
            mClusterManager.onCameraIdle()
        }

        mClusterManager.setOnClusterClickListener {
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    it.position, floor((mMap.cameraPosition.zoom + 1).toDouble()).toFloat()
                ), 300,
                null
            )
            return@setOnClusterClickListener true
        }
    }

    /**
     * Call Api with Location LatLng
     */
    @SuppressLint("MissingPermission")
    fun onLocationChanged(latLng: LatLng) {
        mMap.isMyLocationEnabled = true
        if (viewModel.currentLatLng.value == null) {
            viewModel.currentLatLng.value = latLng
        }
        moveMapToLocation(latLng)
    }

    @SuppressLint("MissingPermission")
    fun onLocationSettingsCancel() {
        mMap.isMyLocationEnabled = true
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-34.921230, 138.599503), 17f))
    }

    /**
     * Move Camera to given LatLng
     */
    private fun moveMapToLocation(latLng: LatLng?) {
        if (latLng != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, CAMERA_ZOOM_CURRENT_LOC))
        }
    }

}
