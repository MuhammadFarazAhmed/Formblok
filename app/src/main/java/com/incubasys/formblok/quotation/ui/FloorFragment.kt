package com.incubasys.formblok.quotation.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.incubasys.formblok.R
import com.incubasys.formblok.common.extenstions.spannedHeading
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.custom.wheelview.adapter.NumericWheelAdapter
import com.incubasys.formblok.quotation.callback.FloorsFragmentCallback
import com.incubasys.formblok.quotation.viewmodel.CreateQuotationViewModel
import kotlinx.android.synthetic.main.fragment_floor.*


class FloorFragment : BaseFragment() {

    private lateinit var callback: FloorsFragmentCallback
    private lateinit var viewModel: CreateQuotationViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FloorsFragmentCallback) callback = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = 0
        viewModel = (activity as CreateQuotationActivity).viewModel
        tempTotalArea = viewModel.propertyOutput.total_liveable_area
        if (viewModel.isRennovated) {
            viewModel.changeRennovatedLiveableArea()
            tempRenovatedTotalArea = viewModel.originalTotalLiveableAreaAfterRenovation.value!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_floor, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as CreateQuotationActivity).setProgressIncrement(40)

        tvFloorHeading.text =
            spannedHeading(
                (activity as CreateQuotationActivity).applicationContext,
                "How many\nfloors?",
                "floors?"
            )

        initWheelView()

        ivFloorBackArrow.setOnClickListener {
            activity?.onBackPressed()
        }

        ivFloorNext.setOnClickListener {
            if (position == 0) {
                viewModel.propertyOutput.floors = 1
                if (viewModel.isRennovated) {
                    viewModel.propertyOutput.total_liveable_area = tempRenovatedTotalArea
                } else {
                    viewModel.propertyOutput.total_liveable_area = tempTotalArea
                }
            } else {
                viewModel.propertyOutput.floors = 2
                if (viewModel.propertyOutput.total_liveable_area > tempTotalArea && viewModel.propertyOutput.total_liveable_area > tempRenovatedTotalArea) {

                } else {
                    if (viewModel.isRennovated) {
                        viewModel.propertyOutput.total_liveable_area = tempRenovatedTotalArea.times(2)
                    } else {
                        viewModel.propertyOutput.total_liveable_area = tempTotalArea.times(2)
                    }
                }
            }
            callback.onNextClicked(CreateQuotationActivity.ACTION.ROOMS)
        }
    }

    private fun initWheelView() {
        wheelView.adapter = NumericWheelAdapter(1, 2)
        wheelView.currentItem = position
        wheelView.setCyclic(false)
        val boldFont = context?.let { ResourcesCompat.getFont(it, R.font.visbycf_bold) }
        val lightFont = context?.let { ResourcesCompat.getFont(it, R.font.visbycf_light) }
        if (boldFont != null) {
            wheelView.setSelectedFont(boldFont)
        }
        if (lightFont != null) {
            wheelView.setOuterFont(lightFont)
        }
        wheelView.setOnItemSelectedListener {
            when (it) {
                0 -> {
                    position = 0
                }
                1 -> {
                    position = 1
                }
            }
        }
    }


    companion object {
        private var position = 0
        private var tempTotalArea = 0.0
        private var tempRenovatedTotalArea = 0.0
        @JvmStatic
        fun newInstance() =
            FloorFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
