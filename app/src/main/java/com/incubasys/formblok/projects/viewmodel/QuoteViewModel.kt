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

class QuoteViewModel @Inject constructor(
    application: Application,
    private val parseErrors: ParseErrors,
    private val projectsRepository: ProjectsRepository
) : BaseViewModel(application, parseErrors) {

    var quoteDetailList = mutableListOf<ProjectDetailItem>()
    var propertyOutput = MutableLiveData<PropertyOutput>()
    var materailNameList = mutableListOf<String>()
    var isAddress = false

    fun setUpQuoteDetailList() {
        with(quoteDetailList) {
            add(0, ProjectDetailItem(1, ProjectDetailItemAdapter.TYPE_HEADER, "Construction Information"))
            add(
                1,
                ProjectDetailItem(
                    2,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "External Walls",
                    if (materailNameList.size > 0) materailNameList[0] else ""
                )
            )
            add(
                2,
                ProjectDetailItem(
                    3,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Window Frames",
                    if (materailNameList.size > 1) materailNameList[1] else ""
                )
            )
            add(
                3,
                ProjectDetailItem(
                    4,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Glazing Type",
                    if (materailNameList.size > 2) materailNameList[2] else ""
                )
            )
            add(
                4,
                ProjectDetailItem(
                    5,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Internal Finishes",
                    if (materailNameList.size > 3) materailNameList[3] else ""
                )
            )
            add(
                5,
                ProjectDetailItem(
                    6,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Floor Finishes",
                    if (materailNameList.size > 4) materailNameList[4] else ""
                )
            )
            add(
                6,
                ProjectDetailItem(
                    7,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Cabinetry Finished",
                    if (materailNameList.size > 5) materailNameList[5] else ""
                )
            )
        }
    }

    fun getFirstMaterial() {
       // if (!isAddress) {
            propertyOutput.value?.material_category?.types?.forEachIndexed { index, materialTypeWithData ->
                // val projectDetailItem = quoteDetailList[index]
               // if (materialTypeWithData.materials.size > 0) {
                    materailNameList.add(index, materialTypeWithData.material.name)
                //}
            }
        /*} else {
            propertyOutput.value?.materialTypeWithData?.forEachIndexed { index, materialTypeWithData ->
                // val projectDetailItem = quoteDetailList[index]
                //if (materialTypeWithData.materials.size > 0) {
                    materailNameList.add(index, materialTypeWithData.materials.name)
                //}
            }
        }*/
    }
}

