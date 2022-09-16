package com.incubasys.formblok.projects.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.incubasys.formblok.R
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.projects.adapter.ProjectDetailItemAdapter
import com.incubasys.formblok.projects.data.model.PropertyOutput
import com.incubasys.formblok.projects.viewmodel.AreaViewModel
import kotlinx.android.synthetic.main.area_fragment.*
import javax.inject.Inject

class AreaFragment : BaseFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var viewModel: AreaViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(AreaViewModel::class.java)
        arguments?.let {
            viewModel.propertyOutput.value = it.getParcelable(ARG_PROPERTY_OUTPUT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.area_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val detailAdapter = ProjectDetailItemAdapter()
        rvAreaDetail.layoutManager = LinearLayoutManager(activity)
        rvAreaDetail.adapter = detailAdapter
        detailAdapter.submitList(viewModel.areaDetailList)

        viewModel.propertyOutput.observe(this, Observer {
            viewModel.areaDetailList.clear()
            viewModel.setUpAreaDetailList()
            detailAdapter.submitList(viewModel.areaDetailList)

            when (it.construction_style) {
                0 -> {
                    selectByDefault()
                }
                1 -> {
                    rbContemporary.isChecked = true
                    rbContemporary.setTextColor(
                        ContextCompat.getColor(context!!, R.color.white)
                    )
                }
            }

            if (it.isAddress) {
                rbTraditional.isClickable = true
                rbContemporary.isClickable = true
            } else {
                disableRadioClicks()
            }

        })

        selectByDefault()

        rbTraditional.setOnCheckedChangeListener { _, b ->
            if (b) {
                rbTraditional.setTextColor(
                    ContextCompat.getColor(context!!, R.color.white)
                )
                rbContemporary.setTextColor(
                    ContextCompat.getColor(context!!, R.color.colorPrimary)
                )
                viewModel.propertyOutput.value?.construction_style = 0
            }
        }

        rbContemporary.setOnCheckedChangeListener { _, b ->
            if (b) {
                rbTraditional.setTextColor(
                    ContextCompat.getColor(context!!, R.color.colorPrimary)
                )
                rbContemporary.setTextColor(
                    ContextCompat.getColor(context!!, R.color.white)
                )
                viewModel.propertyOutput.value?.construction_style = 1
            }

        }
    }

    private fun selectByDefault() {
        rbTraditional.isChecked = true
        rbTraditional.setTextColor(
            ContextCompat.getColor(context!!, R.color.white)
        )
    }

    fun disableRadioClicks() {
            rbTraditional.isClickable = false
        rbContemporary.isClickable = false
    }

    companion object {
        private const val ARG_PROPERTY_OUTPUT = "property_output"

        @JvmStatic
        fun newInstance(propertyOutput: PropertyOutput) =
            AreaFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PROPERTY_OUTPUT, propertyOutput)
                }
            }
    }

}
