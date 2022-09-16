package com.incubasys.formblok.common.data.model

import com.google.gson.annotations.SerializedName

data class SupportContentOutput(
    @SerializedName("id") var id: Int,
    @SerializedName("privacy_policy") var privacyPolicy: String,
    @SerializedName("terms") var terms: String
)
