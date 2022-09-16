package com.incubasys.formblok.projects.ui

import android.annotation.SuppressLint
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
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.common.ui.prefixDollarSign
import com.incubasys.formblok.projects.adapter.ProjectDetailItemAdapter
import com.incubasys.formblok.projects.data.model.PropertyOutput
import com.incubasys.formblok.projects.viewmodel.QuoteViewModel
import kotlinx.android.synthetic.main.quote_fragment.*
import javax.inject.Inject

class QuoteFragment : BaseFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    val viewModel: QuoteViewModel by lazy {
        ViewModelProviders.of(this, factory)
            .get(QuoteViewModel::class.java)
    }

    private lateinit var binding: com.incubasys.formblok.databinding.QuoteFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.propertyOutput.value = it.getParcelable(ARG_PROPERTY_OUTPUT)
            viewModel.isAddress = it.getBoolean(ARG_IS_ADDRESS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.quote_fragment, container, false)
        binding.vm = viewModel
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val detailAdapter = ProjectDetailItemAdapter()
        rvQuoteDetails.layoutManager = LinearLayoutManager(activity)
        rvQuoteDetails.adapter = detailAdapter

        viewModel.propertyOutput.observe(this, Observer {
            if(viewModel.isAddress){
                tvQuoteHeadLabel.text = prefixDollarSign(it.material_category.totalQuotePrice)
            }else{
                tvQuoteHeadLabel.text = prefixDollarSign(it.total_cost)
            }
            viewModel.getFirstMaterial()
            viewModel.setUpQuoteDetailList()
            detailAdapter.submitList(viewModel.quoteDetailList)
        })
    }

    companion object {
        private const val ARG_PROPERTY_OUTPUT = "property_output"
        private const val ARG_IS_ADDRESS = "ISADDRESS"

        @JvmStatic
        fun newInstance(propertyOutput: PropertyOutput, isAddress: Boolean) =
            QuoteFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PROPERTY_OUTPUT, propertyOutput)
                    putBoolean(ARG_IS_ADDRESS, isAddress)
                }
            }
    }
}
