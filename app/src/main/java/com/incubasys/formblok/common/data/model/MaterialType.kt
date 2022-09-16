package com.incubasys.formblok.common.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MaterialType(
    var id: Int = -1,
    var name: String = "",
    var materials: MutableList<ConstructionMaterialOutput> = mutableListOf()
) : Parcelable {

}