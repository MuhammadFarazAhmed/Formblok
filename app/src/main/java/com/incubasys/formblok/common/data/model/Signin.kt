package com.incubasys.formblok.common.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Signin(
    @SerializedName("email_or_phone") val emailOfPhone: String
    , @SerializedName("password") var password: String?
) : Parcelable {

}