package com.incubasys.formblok.projects.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UtilityInfo(
    var rural_water_corporation: String = "",
    var melbourne_water_retailer: String = "",
    var melbourne_water: String = "",
    var power_distributor: String = ""
) : Parcelable {

}
