package com.incubasys.formblok.quotation.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.incubasys.formblok.R
import com.incubasys.formblok.common.extenstions.spannedHeading
import com.incubasys.formblok.quotation.callback.DevelopmentTypeFragmentCallback
import com.incubasys.formblok.quotation.viewmodel.CreateQuotationViewModel
import kotlinx.android.synthetic.main.fragment_development_type.*


class DevelopmentTypeFragment : Fragment() {

    private lateinit var viewModel: CreateQuotationViewModel
    private lateinit var callback: DevelopmentTypeFragmentCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DevelopmentTypeFragmentCallback) callback = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_development_type, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as CreateQuotationActivity).viewModel
        (activity as CreateQuotationActivity).setProgressIncrement(20)

        tvDevelopmentTypeHeading.text =
            spannedHeading(
                (activity as CreateQuotationActivity).applicationContext,
                "What type of\ndevelopment?",
                "development?"
            )

        ivDevelopmentTypeBackArrow.setOnClickListener {
            activity?.onBackPressed()
        }

        ivSingle.setOnClickListener {
            it.isSelected = true
            ivDouble.isSelected = false
            ivMulti.isSelected = false
            ivApartments.isSelected = false
            viewModel.propertyOutput.development_type = 0
            viewModel.changeValuesAccordingToDevelopmentType()
        }
        ivDouble.setOnClickListener {
            it.isSelected = true
            ivSingle.isSelected = false
            ivMulti.isSelected = false
            ivApartments.isSelected = false
            viewModel.propertyOutput.development_type = 1
            viewModel.changeValuesAccordingToDevelopmentType()
        }
        ivMulti.setOnClickListener {
            /*  it.isSelected = true
              ivDouble.isSelected = false
              ivSingle.isSelected = false
              ivApartments.isSelected = false
              viewModel.propertyOutput.development_type = 2*/
            Toast.makeText(activity, "App feature not currently available", Toast.LENGTH_SHORT).show()
        }
        ivApartments.setOnClickListener {
            /*it.isSelected = true
            ivDouble.isSelected = false
            ivMulti.isSelected = false
            ivSingle.isSelected = false
            viewModel.propertyOutput.development_type = 3*/
            Toast.makeText(activity, "app feature not currently available", Toast.LENGTH_SHORT).show()
        }

        ivDevelopmentTypeNext.setOnClickListener {
            if (viewModel.propertyOutput.development_type == -1) {
                Toast.makeText(activity, "Please Select one option", Toast.LENGTH_SHORT).show()
            } else {
                callback.onNextClicked(CreateQuotationActivity.ACTION.FLOORS)
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            DevelopmentTypeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
