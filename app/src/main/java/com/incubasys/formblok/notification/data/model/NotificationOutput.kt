package com.incubasys.formblok.notification.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationOutput(
    @SerializedName("id") var id: Int = -1,
    @SerializedName("user_name") var type: Int,
    @SerializedName("resource_id") var resourceID: Int = -1,
    @SerializedName("notification_type") var notificationType: Int = -1,
    @SerializedName("body") var body: String = "",
    @SerializedName("is_read") var isRead: Boolean = false

) : Parcelable