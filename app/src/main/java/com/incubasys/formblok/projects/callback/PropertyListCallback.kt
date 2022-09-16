package com.incubasys.formblok.projects.callback

import com.incubasys.formblok.projects.data.model.PropertyMinimal

interface PropertyListCallback {

    fun onPropertySelected(propertyMinimal: PropertyMinimal)
}