package com.incubasys.formblok.projects.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.incubasys.formblok.R
import com.incubasys.formblok.common.callback.RecyclerViewCallback
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.FragmentProjectDetailBinding
import com.incubasys.formblok.projects.adapter.PropertyAdapter
import com.incubasys.formblok.projects.callback.PropertyListCallback
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.projects.viewmodel.PropertyListViewModel
import kotlinx.android.synthetic.main.fragment_project_detail.*
import javax.inject.Inject

private const val ARG_PROJECT = "project"


class PropertyListFragment : BaseFragment(), RecyclerViewCallback {

    override fun onListItemClicked(position: Int) {
        propertyListCallback.onPropertySelected(viewModel.project.value?.properties?.get(position)!!)
    }

    private var project: Project? = null
    private lateinit var binding: FragmentProjectDetailBinding
    private lateinit var propertyListCallback: PropertyListCallback

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: PropertyListViewModel by lazy {
        ViewModelProviders.of(this, factory).get(PropertyListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PropertyListCallback) {
            propertyListCallback = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            project = it.getParcelable(ARG_PROJECT)
            viewModel.project.value = project
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_detail, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = PropertyAdapter(this)
        rvProperties.layoutManager = LinearLayoutManager(context)
        rvProperties.adapter = adapter

        ivPropertyDetailArrowBack.setOnClickListener {
            activity?.onBackPressed()
        }

        viewModel.project.observe(this, Observer { project ->
            tvProjectTitle.text = project.name

            if (project.properties?.size!! > 0) {
                tvProjectCity.text = project.properties?.get(0)?.address
            }
            adapter.submitList(project.properties)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(project: Project) =
            PropertyListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PROJECT, project)
                }
            }
    }
}
