package com.incubasys.formblok.quotation.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.incubasys.formblok.common.extenstions.spannedHeading
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.BuildTypeFragmentBinding
import com.incubasys.formblok.quotation.callback.BuildTypeFragmentCallback
import com.incubasys.formblok.quotation.viewmodel.CreateQuotationViewModel
import kotlinx.android.synthetic.main.build_type_fragment.*

class BuildTypeFragment : BaseFragment() {

    companion object {
        fun newInstance() =
            BuildTypeFragment()
    }

    private lateinit var binding: BuildTypeFragmentBinding
    private lateinit var callback: BuildTypeFragmentCallback
    private lateinit var viewModel: CreateQuotationViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BuildTypeFragmentCallback) callback = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BuildTypeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as CreateQuotationActivity).viewModel
        (activity as CreateQuotationActivity).setProgressIncrement(0)

        tvBuildTypeHeading.text =
            spannedHeading((activity as CreateQuotationActivity).applicationContext, "What type of\nBuild?", "Build?")

        ivBuildTypeBackArrow.setOnClickListener {
            activity?.setResult(Activity.RESULT_CANCELED)
            activity?.finish()
        }

        ivNewBuild.setOnClickListener {
            it.isSelected = true
            ivRennovation.isSelected = false
            viewModel.propertyOutput.construction_type = 0
        }
        ivRennovation.setOnClickListener {
            it.isSelected = true
            ivNewBuild.isSelected = false
            viewModel.propertyOutput.construction_type = 1
        }

        ivBuildTypeNext.setOnClickListener {
            if (viewModel.propertyOutput.construction_type != -1)
                when (viewModel.propertyOutput.construction_type) {
                    0 -> {
                        viewModel.isRennovated = false
                        callback.onNextClicked(CreateQuotationActivity.ACTION.DEV_TYPE)
                    }
                    1 -> {
                        viewModel.isRennovated = true
                        callback.onNextClicked(CreateQuotationActivity.ACTION.RENOVATION)
                    }
                }
            else
                Toast.makeText(activity, "Select one option", Toast.LENGTH_SHORT).show()
        }
    }

}
