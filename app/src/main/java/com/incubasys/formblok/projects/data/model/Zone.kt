package com.incubasys.formblok.projects.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Zone(
    var id: Int? = null,
    var thumbnail: Int? = null,
    var description: String? = null,
    var code: String? = null,
    var info_url: String? = null
) : Parcelable