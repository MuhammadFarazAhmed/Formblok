package com.incubasys.formblok.notification.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationInput(
    @SerializedName("user_name") var userName: String? = "",
    @SerializedName("resource_id") var resourceID: Int? = -1,
    @SerializedName("notification_type") var notificationType: Int? = -1,
    var comeFromAppLink: Boolean = false
) : Parcelable