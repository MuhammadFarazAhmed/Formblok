package com.incubasys.formblok.common.data.model
data class MaterialData(
    val id: Int,
    val name: String,
    val price: Double,
    val types: MutableList<MaterialType>? = null
)