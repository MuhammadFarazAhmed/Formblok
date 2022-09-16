package com.incubasys.formblok.notification.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.incubasys.formblok.common.data.model.PageInfo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationPage(
    @SerializedName("page_info")
    var pageInfo: PageInfo? = null,
    @SerializedName("notifications")
    var notifications: MutableList<NotificationOutput>? = null
) : Parcelable {

}