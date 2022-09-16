package com.incubasys.formblok.projects.ui

import android.R.color.transparent
import android.content.Context
import android.content.Intent
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
import com.android.billingclient.api.BillingFlowParams
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.incubasys.formblok.R
import com.incubasys.formblok.common.adaptor.TabAdapter
import com.incubasys.formblok.common.data.remote.ApiStatus.*
import com.incubasys.formblok.common.extenstions.getPolygonLatLngBounds
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.AddressDetailFragmentBinding
import com.incubasys.formblok.projects.callback.AddressDetailFragmentCallback
import com.incubasys.formblok.projects.data.model.PropertyInput
import com.incubasys.formblok.projects.data.model.PropertyMinimal
import com.incubasys.formblok.projects.data.model.PropertyOutput
import com.incubasys.formblok.projects.viewmodel.AddressDetailViewModel
import com.incubasys.formblok.util.Constants
import kotlinx.android.synthetic.main.address_detail_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddressDetailFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var polygonOptions = PolygonOptions()

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setPadding(0, 0, 0, 25)
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
        } catch (e: Resources.NotFoundException) {
            Log.e("Map", "Can't find style. Error: ", e)
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(viewModel.latLng, 17f))
        mMap.uiSettings.setAllGesturesEnabled(false)

        viewModel.getAddressDetail(viewModel.addressId.toString())
    }

    private lateinit var binding: AddressDetailFragmentBinding
    private var callback: AddressDetailFragmentCallback? = null

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val adapter: TabAdapter by lazy { TabAdapter(childFragmentManager) }

    val viewModel: AddressDetailViewModel by lazy {
        ViewModelProviders.of(this, factory)
            .get(AddressDetailViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddressDetailFragmentCallback) {
            callback = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.addressId = it.getInt("addressId")
            viewModel.projectId = it.getInt("projectId",-1)
            viewModel.latLng = it.getParcelable("latlng")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.address_detail_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.addressDetailMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()

        if (viewModel.isAddressQuoted) {
            bAddressDetailGetQuote.text = getString(R.string.add_to_project)
        } else {
            bAddressDetailGetQuote.text = getString(R.string.get_quote)
        }

        viewModel.addressDetailLivedata.observe(this, Observer { apiResponse ->
            when (apiResponse.status) {
                LOADING -> {
                    showProgress()
                }
                SUCCESS -> {
                    dismissProgress()
                }
                ERROR -> {
                    dismissProgress()
                    Toast.makeText(activity, apiResponse.error?.message, Toast.LENGTH_SHORT).show()
                    activity?.finish()
                }
                EMPTY -> {
                }
                COMPLETED -> {
                }
            }
        })

        viewModel.apiResponse.observe(this, Observer {
            when (it.status) {
                LOADING -> {
                    showProgress()
                }
                SUCCESS -> {
                    dismissProgress()
                    it.data?.let {project ->
                        callback?.openPropertyDetailDirect(project,  project.properties?.get(project.properties!!.size - 1))
                    }
                }
                EMPTY -> {
                }
                ERROR -> {
                    dismissProgress()
                    Toast.makeText(activity, it.error?.message, Toast.LENGTH_SHORT).show()
                }
                COMPLETED -> {}
            }
        })

        viewModel.propertyOutput.observe(this, Observer {
            addPolygon(it)
            tvAddress.text = it.address
            tvArea.text = it.area
            adapter.flushFragments()
            adapter.addFragment(InfoFragment.newInstance(it), "info")
            adapter.addFragment(DetailFragment.newInstance(it), "details")
            adapter.addFragment(AreaFragment.newInstance(it), "area")
            if (viewModel.isAddressQuoted) {
                it.isAddress = false
                adapter.addFragment(QuoteFragment.newInstance(it, true), "quote")
            } else {
                it.isAddress = true
            }
            vpAddressDetailPager.adapter = adapter
            tlAddressDetailTabLayout.setupWithViewPager(vpAddressDetailPager)
            vpAddressDetailPager.offscreenPageLimit = 3
            if (viewModel.isAddressQuoted) {
                vpAddressDetailPager.setCurrentItem(3, true)
            } else {
                vpAddressDetailPager.setCurrentItem(0, true)
            }
        })

        bAddressDetailGetQuote.setOnClickListener {
            if (!viewModel.isAddressQuoted) {
                viewModel.checkSubscription()
               // callback?.openQuotationActivity(viewModel.propertyOutput.value!!)
            } else {
                if (viewModel.projectId == -1) {
                    callback?.openAddToProjectActivity()
                } else {
                    viewModel.propertyOutput.value?.toPropertyInput(
                        PropertyInput()
                    )?.let { it1 -> viewModel.addAPropertyToProject(viewModel.projectId, it1) }
                }
            }
        }

        /**
         * Subscription
         */
        viewModel.checkSubscriptionLiveData.observe(this, Observer {
            when (it.status) {
                LOADING -> showProgress()
                SUCCESS -> {
                    dismissProgress()
                    callback?.openQuotationActivity(viewModel.propertyOutput.value!!)
                }
                ERROR -> {
                    dismissProgress()
                     /*if (it.error?.code == 403) {
                         viewModel.fetchSkuForSubscription()
                     } else {
                         Toast.makeText(activity, it.error?.message, Toast.LENGTH_SHORT).show()
                     }*/
                    viewModel.fetchSkuForSubscription()
                }
                else -> {
                }
            }
        })

        viewModel.gotASkuLiveData.observe(this, Observer {
            if (it) {
                val billingFlowParams =
                    BillingFlowParams.newBuilder().setSkuDetails(viewModel.getSku()).build()
                viewModel.getBillingClient().launchBillingFlow(activity, billingFlowParams)
            }
        })

        viewModel.subscriptionResponse.observe(this, Observer {
            when (it.status) {
                LOADING -> showProgress()
                SUCCESS -> {
                    dismissProgress()
                    callback?.openQuotationActivity(viewModel.propertyOutput.value!!)
                }
                ERROR -> {
                    dismissProgress()
                    Toast.makeText(activity, it.error?.message, Toast.LENGTH_SHORT).show()
                }
                EMPTY -> {
                }
                COMPLETED -> {
                }
            }
        })

    }

    private fun addPolygon(it: PropertyOutput) {
        val poliList = ArrayList<LatLng>()
        it.polygon.forEach { poly ->
            polygonOptions.add(poly.toLatLng())
            poliList.add(poly.toLatLng())
        }

        polygonOptions.fillColor(
            activity?.applicationContext?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.sea_blue
                )
            }!!
        )

        mMap.addPolygon(
            polygonOptions
                .clickable(true)
        )
        val latLngBounds = getPolygonLatLngBounds(poliList)
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200))
    }

    fun updatePropertyOutput(propertyOutput: PropertyOutput) {
        viewModel.propertyOutput.value = propertyOutput
    }

    fun addQuoteFragment() {
        viewModel.isAddressQuoted = true
        viewModel.propertyOutput.value?.total_cost =
            viewModel.propertyOutput.value?.material_category?.totalQuotePrice ?: 0.0
        bAddressDetailGetQuote.text = getString(R.string.add_to_project)
    }


    private fun initToolbar() {

        ivToolbarBackArrow.setImageResource(R.drawable.ic_share)
        ivtoolbarShare.setImageResource(R.drawable.ic_cross)

        toolbarContainer.setBackgroundColor(ContextCompat.getColor(requireContext(), transparent))

        ivToolbarBackArrow.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
                putExtra(Intent.EXTRA_TEXT, viewModel.propertyOutput.value?.shareUrl)
            }

            val chooser = Intent.createChooser(intent, "Share")
            if (activity?.packageManager?.let { it1 -> intent.resolveActivity(it1) } != null) {
                startActivity(chooser)
            }
        }
        ivtoolbarShare.setOnClickListener {
            activity?.onBackPressed()
        }
    }


    companion object {
        fun newInstance(projectId: Int = -1, addressId: Int, latLng: LatLng) =
            AddressDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("addressId", addressId)
                    putInt("projectId", projectId)
                    putParcelable("latlng", latLng)
                }
            }
    }

}
