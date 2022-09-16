package com.incubasys.formblok.projects.ui


import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.incubasys.formblok.R
import com.incubasys.formblok.common.extenstions.spannedHeading
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.common.ui.superscript
import com.incubasys.formblok.databinding.FragmentRenovationDetailBinding
import com.incubasys.formblok.quotation.callback.RenovationFragmentCallback
import com.incubasys.formblok.quotation.ui.CreateQuotationActivity
import com.incubasys.formblok.quotation.viewmodel.CreateQuotationViewModel
import kotlinx.android.synthetic.main.fragment_renovation_detail.*
import kotlinx.android.synthetic.main.fragment_room_detail.*

class RenovationDetailFragment : BaseFragment() {

    private lateinit var viewModel: CreateQuotationViewModel
    private lateinit var callback: RenovationFragmentCallback
    private lateinit var binding: FragmentRenovationDetailBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RenovationFragmentCallback) callback = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_renovation_detail, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = (activity as CreateQuotationActivity).viewModel
        binding.vm = viewModel

        tvRenovationDetailHeading.text =
            spannedHeading(
                (activity as CreateQuotationActivity).applicationContext,
                "Enter renovation \ndetails",
                "details"
            )

        ivRenovationDetailBackArrow.setOnClickListener {
            activity?.onBackPressed()
        }

        ivRenovationDetailNext.setOnClickListener {
            addData()
        }

        etBuiltArea.setOnFocusChangeListener { _, b ->
            if (etBuiltArea.text.isNotEmpty()) {
                if (b) {
                    if (etBuiltArea.text.contains("m2")) {
                        etBuiltArea.text = etBuiltArea.text.delete(etBuiltArea.length() - 2, etBuiltArea.length())
                    }
                } else {
                    etBuiltArea.text = SpannableStringBuilder().apply {
                        append(etBuiltArea.text)
                        append(superscript("m2"))
                    }
                }
            }
        }

        etDemolishedArea.setOnFocusChangeListener { _, b ->
            if (etDemolishedArea.text.isNotEmpty()) {
                if (b) {
                    if (etDemolishedArea.text.contains("m2")) {
                        etDemolishedArea.text =
                            etDemolishedArea.text.delete(etDemolishedArea.length() - 2, etDemolishedArea.length())
                    }
                } else {
                    etDemolishedArea.text = SpannableStringBuilder().apply {
                        append(etDemolishedArea.text)
                        append(superscript("m2"))
                    }
                }
            }
        }

        etDemolishedArea.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                addData()
            }
            return@setOnEditorActionListener false
        }

    }

    private fun addData() {
        if (viewModel.bothFieldsValidate()) {
            viewModel.propertyOutput.already_built_area =
                if (viewModel.builtArea.get()!!.contains("m2")) {
                    viewModel.builtArea.get()?.dropLast(2)!!.toDouble()
                } else {
                    viewModel.builtArea.get()!!.toDouble()
                }

            viewModel.propertyOutput.demolish_area =
                if (viewModel.demolisedArea.get()!!.contains("m2")) {
                    viewModel.demolisedArea.get()?.dropLast(2)!!.toDouble()
                } else {
                    viewModel.demolisedArea.get()!!.toDouble()
                }
            callback.onNextClicked(CreateQuotationActivity.ACTION.DEV_TYPE)
        } else {
            Toast.makeText(
                (activity as CreateQuotationActivity).applicationContext,
                "Please fill the require fields with valid inputs",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RenovationDetailFragment().apply {

            }
    }
}
