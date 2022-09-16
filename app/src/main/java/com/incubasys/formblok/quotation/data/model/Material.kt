package com.incubasys.formblok.quotation.data.model

import com.incubasys.formblok.common.data.model.ConstructionMaterialOutput

data class Material(
    var id: Int = -1, var type: Int = -1, var heading: String = "",
    val list: MutableList<ConstructionMaterialOutput>? = null)