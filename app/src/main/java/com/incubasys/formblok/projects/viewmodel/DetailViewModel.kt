package com.incubasys.formblok.projects.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.projects.adapter.ProjectDetailItemAdapter
import com.incubasys.formblok.projects.data.ProjectsRepository
import com.incubasys.formblok.projects.data.model.ProjectDetailItem
import com.incubasys.formblok.projects.data.model.PropertyOutput
import com.incubasys.formblok.util.ParseErrors
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    application: Application,
    private val parseErrors: ParseErrors,
    private val projectsRepository: ProjectsRepository
) : BaseViewModel(application, parseErrors) {

    var detailList = mutableListOf<ProjectDetailItem>()
    var propertyOutput = MutableLiveData<PropertyOutput>()

    fun setUpProjectSubDetailList() {
        with(detailList, {
            add(0, ProjectDetailItem(1, ProjectDetailItemAdapter.TYPE_HEADER, "Plot Information"))
            add(
                1,
                ProjectDetailItem(
                    2,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Lot/Plan Number",
                    propertyOutput.value?.lot_number
                )
            )
            add(
                2,
                ProjectDetailItem(
                    3,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Council Property Number",
                    propertyOutput.value?.property_number
                )
            )
            add(3, ProjectDetailItem(4, ProjectDetailItemAdapter.TYPE_HEADER, "Utility Information"))
            add(
                4,
                ProjectDetailItem(
                    5,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Rural Water Corporation",
                    propertyOutput.value?.utilityInfomation?.rural_water_corporation
                )
            )
            add(
                5,
                ProjectDetailItem(
                    6,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Melbourne Water Retailer",
                    propertyOutput.value?.utilityInfomation?.melbourne_water_retailer
                )
            )
            add(
                6,
                ProjectDetailItem(
                    7,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Melbourne Water",
                    propertyOutput.value?.utilityInfomation?.melbourne_water
                )
            )
            add(
                7,
                ProjectDetailItem(
                    8,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Power Distributor",
                    propertyOutput.value?.utilityInfomation?.power_distributor
                )
            )
        })
    }
}