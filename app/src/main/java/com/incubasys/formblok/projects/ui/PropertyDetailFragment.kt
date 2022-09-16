package com.incubasys.formblok.projects.ui


import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.incubasys.formblok.R
import com.incubasys.formblok.common.adaptor.TabAdapter
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.extenstions.getPolygonLatLngBounds
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.FragmentPropertyDetailBinding
import com.incubasys.formblok.projects.callback.PropertyDetailFragmentCallback
import com.incubasys.formblok.projects.data.model.PropertyOutput
import com.incubasys.formblok.projects.viewmodel.PropertyDetailViewModel
import kotlinx.android.synthetic.main.fragment_property_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


class PropertyDetailFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var binding: FragmentPropertyDetailBinding
    private lateinit var callback: PropertyDetailFragmentCallback
    private var polygonOptions = PolygonOptions()

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: PropertyDetailViewModel by lazy {
        ViewModelProviders.of(this, factory).get(PropertyDetailViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PropertyDetailFragmentCallback) callback = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.propertyId = it.getInt(ARG_PROPERTY_ID)
            viewModel.comeFromSelectProjectFragment = it.getBoolean(ARG_BOOLEAN)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_property_detail, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.projectDetailMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.propertyOutput.observe(this, Observer {
            when (it.status) {
                ApiStatus.LOADING -> showProgress()
                ApiStatus.SUCCESS -> {
                    dismissProgress()
                    it.data?.let { it1 -> addPolygon(it1) }
                    tvPropertyTitle.text = it.data?.address
                    tvPropertyArea.text = it.data?.area
                    it.data?.let { it1 -> initViewPager(it1) }
                }
                ApiStatus.ERROR -> {
                    dismissProgress()
                    Toast.makeText(activity, it.error?.message, Toast.LENGTH_SHORT).show()
                    activity?.finish()
                }
                else -> {

                }
            }
        })

        ivToolbarBackArrow.setOnClickListener {
            if (viewModel.comeFromSelectProjectFragment) {
                activity?.finish()
            } else {
                activity?.onBackPressed()
            }
        }

        ivtoolbarShare.setOnClickListener {
            callback.onShareIconClicked()
        }
    }

    private fun addPolygon(it: PropertyOutput) {
        val poliList = ArrayList<LatLng>()
        it.polygon.forEach { poly ->
            polygonOptions.add(poly.toLatLng())
            poliList.add(poly.toLatLng())
        }
        polygonOptions.fillColor(
            activity?.applicationContext?.let { it1 -> ContextCompat.getColor(it1, R.color.sea_blue) }!!
        )
        mMap.addPolygon(
            polygonOptions
                .clickable(true)
        )
        val latLngBounds = getPolygonLatLngBounds(poliList)
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200))
    }

    private fun initViewPager(propertyOutput: PropertyOutput) {
        val adapter = TabAdapter(childFragmentManager)

        adapter.addFragment(InfoFragment.newInstance(propertyOutput), "info")
        adapter.addFragment(DetailFragment.newInstance(propertyOutput), "details")
        adapter.addFragment(AreaFragment.newInstance(propertyOutput), "area")
        adapter.addFragment(QuoteFragment.newInstance(propertyOutput, false), "quote")

        vpProjectDetailPager.adapter = adapter
        tlProjectDetailTabLayout.setupWithViewPager(vpProjectDetailPager)
        vpProjectDetailPager.offscreenPageLimit = 4
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        /**
         * Adding style to Map
         */
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    activity, R.raw.map_style
                )
            )
            if (!success) {
                Log.e("Map", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("Map", "Can't find style. Error: ", e)
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 17f))
        mMap.uiSettings.setAllGesturesEnabled(false)


        viewModel.getPropertyDetailById()
    }


    companion object {
        private const val ARG_PROPERTY_ID = "propertyId"
        private const val ARG_BOOLEAN = "boolean"

        @JvmStatic
        fun newInstance(propertyId: Int, comeFromSelectableProjectFragment: Boolean = false) =
            PropertyDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PROPERTY_ID, propertyId)
                    putBoolean(ARG_BOOLEAN, comeFromSelectableProjectFragment)
                }
            }
    }
}
