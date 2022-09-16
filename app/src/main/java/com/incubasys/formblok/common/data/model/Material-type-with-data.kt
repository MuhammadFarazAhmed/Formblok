package com.incubasys.formblok.common.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MaterialTypeProperty(
    var id: Int = -1,
    var name: String = "",
    var material: ConstructionMaterialOutput = ConstructionMaterialOutput()
) : Parcelable {

}