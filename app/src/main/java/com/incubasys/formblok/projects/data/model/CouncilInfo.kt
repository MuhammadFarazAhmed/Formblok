package com.incubasys.formblok.projects.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CouncilInfo(
    var local_council: String = "",
    var legislative_council: String = "",
    var legislative_assembly: String = ""
) : Parcelable {

}
