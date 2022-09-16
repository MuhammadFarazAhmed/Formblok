package com.incubasys.formblok.explore.data.model

import com.google.gson.annotations.SerializedName

data class AddressShort(
    @SerializedName("id") var id: Int,
    @SerializedName("lat") var lat: Double,
    @SerializedName("lng") var lng: Double,
    @SerializedName("lot_number") var lotNumber: String,
    @SerializedName("property_number") var propertyNumber: String,
    @SerializedName("site_area") var siteArea: Double
)
