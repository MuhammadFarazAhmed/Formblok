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

class AreaViewModel @Inject constructor(
    application: Application,
    private val parseErrors: ParseErrors,
    private val projectsRepository: ProjectsRepository
) : BaseViewModel(application, parseErrors) {

    var areaDetailList = mutableListOf<ProjectDetailItem>()
    val propertyOutput = MutableLiveData<PropertyOutput>()

    fun setUpAreaDetailList() {
        with(areaDetailList) {
            add(0, ProjectDetailItem(1, ProjectDetailItemAdapter.TYPE_HEADER, "Site Information"))
            add(
                1,
                ProjectDetailItem(
                    2,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Land/Site Area",
                    propertyOutput.value?.site_area.toString(), isSpanned = true
                )
            )
            add(
                2,
                ProjectDetailItem(
                    3,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Garden Area",
                    propertyOutput.value?.garden_area.toString(), isSpanned = true,
                    isPercentageShown = true,
                    areaPercentage = propertyOutput.value?.garden_area_percentage!!
                )
            )
            add(
                3,
                ProjectDetailItem(
                    4,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "minimum North facing Area",
                    propertyOutput.value?.north_area.toString(), isSpanned = true,
                    isPercentageShown = true,
                    areaPercentage = propertyOutput.value?.north_area_percentage!!
                )
            )
            add(
                4,
                ProjectDetailItem(
                    6,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Potential Driveway Area",
                    propertyOutput.value?.driveway_area.toString(), isSpanned = true
                )
            )
            add(5, ProjectDetailItem(5, ProjectDetailItemAdapter.TYPE_HEADER, "Liveable Area"))
            add(
                6,
                ProjectDetailItem(
                    7,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Liveable Ground Floor Area",
                    propertyOutput.value?.ground_floor_area.toString(), isSpanned = true
                )
            )
            add(
                7,
                ProjectDetailItem(
                    8,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Open Liveable Area (Alfresco)",
                    propertyOutput.value?.open_liveable_area.toString(), isSpanned = true
                )
            )
            add(
                8,
                ProjectDetailItem(
                    9,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Total Liveable area",
                    propertyOutput.value?.total_liveable_area.toString(), isSpanned = true
                )
            )
        }
    }
}
