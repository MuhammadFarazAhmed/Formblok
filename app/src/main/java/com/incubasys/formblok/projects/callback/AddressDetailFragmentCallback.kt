package com.incubasys.formblok.projects.callback

import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.projects.data.model.PropertyMinimal
import com.incubasys.formblok.projects.data.model.PropertyOutput

interface AddressDetailFragmentCallback {
    fun openQuotationActivity(propertyOutput: PropertyOutput?)
    fun openAddToProjectActivity()
    fun openPropertyDetailDirect(project: Project, propertyMinimal: PropertyMinimal?)
}