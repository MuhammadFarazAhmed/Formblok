package com.incubasys.formblok.projects.callback

import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.projects.data.model.PropertyMinimal

interface SelectProjectFragmentCallback {

    fun openPorpertyDetailActivity(project: Project, propertyMinimal: PropertyMinimal?)
}