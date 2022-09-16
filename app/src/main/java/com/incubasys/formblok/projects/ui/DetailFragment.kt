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
import com.incubasys.formblok.projects.data.model.PropertyOutput
import com.incubasys.formblok.projects.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.detail_fragment.*
import javax.inject.Inject

class DetailFragment : BaseFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    val viewModel: DetailViewModel by lazy {
        ViewModelProviders.of(this, factory)
            .get(DetailViewModel::class.java)
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
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val detailAdapter = ProjectDetailItemAdapter()

        viewModel.propertyOutput.observe(this, Observer {
            viewModel.setUpProjectSubDetailList()
            detailAdapter.submitList(viewModel.detailList)
        })

        rvProjectSubDetails.layoutManager = LinearLayoutManager(activity)
        rvProjectSubDetails.adapter = detailAdapter

    }

    companion object {
        private const val ARG_PROPERTY_OUTPUT = "property_output"

        @JvmStatic
        fun newInstance(propertyOutput: PropertyOutput) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PROPERTY_OUTPUT, propertyOutput)
                }
            }
    }

}
