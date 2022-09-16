package com.incubasys.formblok.projects.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.extenstions.spannedHeading
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.CreateProjectFragmentBinding
import com.incubasys.formblok.projects.callback.CreateProjectFragmentCallback
import com.incubasys.formblok.projects.data.model.PropertyInput
import com.incubasys.formblok.projects.viewmodel.CreateProjectViewModel
import kotlinx.android.synthetic.main.create_project_fragment.*
import javax.inject.Inject

class CreateProjectFragment : BaseFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProviders.of(this, factory).get(CreateProjectViewModel::class.java)
    }

    companion object {
        fun newInstance(propertyInput: PropertyInput?) = CreateProjectFragment().apply {
            arguments = Bundle().apply {
                putParcelable("property", propertyInput)
            }
        }
    }

    private lateinit var binding: CreateProjectFragmentBinding
    private lateinit var callback: CreateProjectFragmentCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CreateProjectFragmentCallback) callback = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.property.value = it.getParcelable("property")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CreateProjectFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = viewModel
        tvCreateProjectHeading.text =
            activity?.let { spannedHeading(it, "What is the projects \ncalled?", "called?") }

        ivCreateProjectBackArrow.setOnClickListener {
            activity?.onBackPressed()
        }

        ivCreateProjectNext.setOnClickListener {
            viewModel.createProject()
        }

        viewModel.property.observe(this, Observer {
            viewModel.projectInput.property = it
        })

        viewModel.projectOutput.observe(this, Observer {
            when (it.status) {
                ApiStatus.LOADING -> showProgress()
                ApiStatus.SUCCESS -> {
                    dismissProgress()
                    callback.onNextButtonClicked(it.data!!)
                }
                ApiStatus.ERROR -> {
                    dismissProgress()
                }
                else -> {

                }
            }
        })

    }

}
