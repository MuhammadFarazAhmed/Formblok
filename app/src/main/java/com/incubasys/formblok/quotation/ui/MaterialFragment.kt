package com.incubasys.formblok.quotation.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.incubasys.formblok.R
import com.incubasys.formblok.common.data.model.MaterialTypeProperty
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.extenstions.spannedHeading
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.projects.data.model.MaterialCategoryOutput
import com.incubasys.formblok.quotation.adapter.MaterialPagerAdapter
import com.incubasys.formblok.quotation.callback.MaterialFragmentCallback
import com.incubasys.formblok.quotation.viewmodel.CreateQuotationViewModel
import kotlinx.android.synthetic.main.fragment_material.*


class MaterialFragment : BaseFragment() {

    private lateinit var viewModel: CreateQuotationViewModel
    private lateinit var adapter: MaterialPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as CreateQuotationActivity).viewModel
        viewModel.getMaterialData()
    }

    private lateinit var callback: MaterialFragmentCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MaterialFragmentCallback) callback = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_material, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = MaterialPagerAdapter(childFragmentManager)

        initFragment()
        subscribeUi()

    }

    private fun initFragment() {
        (activity as CreateQuotationActivity).setProgressIncrement(80)

        tvMaterialHeading.text =
            spannedHeading(
                (activity as CreateQuotationActivity).applicationContext,
                "Select your\nmaterials...",
                "materials..."
            )

        ivMaterialBackArrow.setOnClickListener {
            activity?.onBackPressed()
        }

        bMaterialNext.setOnClickListener {
            constructMaterialCategoryOutput()
            callback.onNextClicked(CreateQuotationActivity.ACTION.DONE)
        }
    }

    private fun constructMaterialCategoryOutput() {
        val materialData = adapter.getMaterialData(vpMaterialPager.currentItem)
        val materialCategoryOutput = MaterialCategoryOutput()
        materialCategoryOutput.id = materialData.id
        materialCategoryOutput.name = materialData.name
        materialCategoryOutput.totalQuotePrice = viewModel.calculatedAreaToShow.value!!.times(materialData.price)
        val temp = mutableListOf<MaterialTypeProperty>()
        materialData.types?.forEach {
            temp.add(MaterialTypeProperty(it.id, it.name, it.materials[0]))
        }
        materialCategoryOutput.types.clear()
        materialCategoryOutput.types.addAll(temp)
        viewModel.propertyOutput.material_category = materialCategoryOutput
    }

    private fun subscribeUi() {
        viewModel.materialData.observe(this, Observer {
            when (it.status) {
                ApiStatus.LOADING -> {
                    showProgress()
                }
                ApiStatus.SUCCESS -> {
                    dismissProgress()
                    it.data?.let { it1 -> adapter.setList(it1) }
                    vpMaterialPager.adapter = adapter
                    tlMaterialTabLayout.setupWithViewPager(vpMaterialPager)
                    adapter.notifyDataSetChanged()

                }
                ApiStatus.ERROR -> {
                    dismissProgress()
                }
                else -> {

                }
            }
        })
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            MaterialFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
