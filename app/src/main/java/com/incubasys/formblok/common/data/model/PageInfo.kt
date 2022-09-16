package com.incubasys.formblok.common.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PageInfo(
    @SerializedName("timestamp")
    var timestamp: String = "",
    @SerializedName("page_size")
    var pageSize: Int? = null,
    @SerializedName("more_available")
    var moreAvailable: Boolean = false,
    @SerializedName("total_records")
    var totalRecords: Int? = null,
    @SerializedName("page")
    var page: Int = 1,
    @SerializedName("total_pages")
    var totalPages: Int? = null
) : Parcelable



