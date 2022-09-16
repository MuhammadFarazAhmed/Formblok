package com.incubasys.formblok.projects.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.incubasys.formblok.R
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.projects.adapter.ProjectDetailItemAdapter
import com.incubasys.formblok.projects.adapter.ZoneAdapter
import com.incubasys.formblok.projects.data.model.PropertyOutput
import com.incubasys.formblok.projects.viewmodel.InfoViewModel
import com.incubasys.formblok.quotation.ui.ZoneListDialogFragment
import kotlinx.android.synthetic.main.fragment_info.*
import javax.inject.Inject


class InfoFragment : BaseFragment() {


    @Inject
    lateinit var factory: ViewModelProvider.Factory

    val viewModel: InfoViewModel by lazy {
        ViewModelProviders.of(this, factory)
            .get(InfoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.propertyOutput.value = it.getParcelable(ARG_PROPERTY_OUTPUT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = ZoneAdapter()
        rvZones.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvZones.adapter = adapter


        val detailAdapter = ProjectDetailItemAdapter()
        rvProjectDetails.layoutManager = LinearLayoutManager(activity)
        rvProjectDetails.adapter = detailAdapter

        ivInfoImage.setOnClickListener {
            val zoneFragment = ZoneListDialogFragment.newInstance(viewModel.zoneList)
            zoneFragment.show(childFragmentManager, zoneFragment.tag)
        }

        viewModel.propertyOutput.observe(this, Observer {
            viewModel.setUpZoneList()
            viewModel.setUpInfoDetailList()
            adapter.submitList(viewModel.zoneList)
            detailAdapter.submitList(viewModel.infoList)
        })

    }


    companion object {
        private const val ARG_PROPERTY_OUTPUT = "property_output"

        @JvmStatic
        fun newInstance(propertyOutput: PropertyOutput) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PROPERTY_OUTPUT, propertyOutput)
                }
            }
    }
}
