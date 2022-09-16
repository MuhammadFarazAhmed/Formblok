package com.incubasys.formblok.projects.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.incubasys.formblok.R
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.projects.adapter.ProjectDetailItemAdapter
import com.incubasys.formblok.projects.data.model.ProjectDetailItem
import com.incubasys.formblok.projects.data.model.PropertyOutput
import com.incubasys.formblok.projects.data.model.Zone
import com.incubasys.formblok.util.ParseErrors
import javax.inject.Inject

class InfoViewModel @Inject constructor(
    application: Application,
    private val parseErrors: ParseErrors
) : BaseViewModel(application, parseErrors) {

    val zoneList by lazy {
        ArrayList<Zone>()
    }
    var propertyOutput = MutableLiveData<PropertyOutput>()
    var infoList = mutableListOf<ProjectDetailItem>()

    fun setUpZoneList() {
        val zones by lazy {
            propertyOutput.value?.zones
        }
        val overlays by lazy {
            propertyOutput.value?.overlays
        }

        zones?.forEachIndexed { index, s ->
            zoneList.add(
                index,
                Zone(id = index, thumbnail = R.drawable.ic_rectangle_seablue, description = s.description,code = s.code,info_url = s.info_url)
            )
        }
        overlays?.forEachIndexed { index, s ->
            zoneList.add(index, Zone(id = index, thumbnail = R.drawable.ic_heritage_zone, description = s.description,code = s.code,info_url = s.info_url))

        }
    }

    fun setUpInfoDetailList() {
        with(infoList) {
            add(0, ProjectDetailItem(1, ProjectDetailItemAdapter.TYPE_HEADER, "Council Information"))
            add(
                1,
                ProjectDetailItem(
                    2,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "LOCAL GOVERNMENT AREA (COUNCIL)",
                    propertyOutput.value?.councilInformation?.local_council,
                    specialTextView = true
                )
            )
            add(
                2,
                ProjectDetailItem(
                    3,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Legislative Council",
                    propertyOutput.value?.councilInformation?.legislative_council,
                    specialTextView = true
                )
            )
            add(
                3,
                ProjectDetailItem(
                    4,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Legislative Assembly",
                    propertyOutput.value?.councilInformation?.legislative_assembly,
                    specialTextView = true
                )
            )
            add(4, ProjectDetailItem(5, ProjectDetailItemAdapter.TYPE_HEADER, "Neighbourhood Information"))
            add(
                5,
                ProjectDetailItem(
                    6,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Nearest School",
                    propertyOutput.value?.neighbourhoodInformation?.school?.name,
                    true,
                    propertyOutput.value?.neighbourhoodInformation?.school?.distance!!,
                    specialTextView = true
                )
            )
            add(
                6,
                ProjectDetailItem(
                    7,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Nearest Parks",
                    propertyOutput.value?.neighbourhoodInformation?.park?.name,
                    true,
                    propertyOutput.value?.neighbourhoodInformation?.park?.distance!!,
                    specialTextView = true
                )
            )
            add(
                7,
                ProjectDetailItem(
                    8,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Nearest Hospital",
                    propertyOutput.value?.neighbourhoodInformation?.hospital?.name,
                    true,
                    propertyOutput.value?.neighbourhoodInformation?.hospital?.distance!!,
                    specialTextView = true
                )
            )
            add(
                8,
                ProjectDetailItem(
                    9,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Nearest Emergency Services",
                    propertyOutput.value?.neighbourhoodInformation?.emergency_services?.name,
                    true,
                    propertyOutput.value?.neighbourhoodInformation?.emergency_services?.distance!!,
                    specialTextView = true
                )
            )
            add(
                9,
                ProjectDetailItem(
                    10,
                    ProjectDetailItemAdapter.TYPE_NORMAL,
                    "Nearest Public Transport",
                    propertyOutput.value?.neighbourhoodInformation?.public_transport?.name,
                    true,
                    propertyOutput.value?.neighbourhoodInformation?.public_transport?.distance!!,
                    specialTextView = true
                )
            )
        }
    }


}