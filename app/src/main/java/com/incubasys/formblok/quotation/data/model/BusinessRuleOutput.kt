package com.incubasys.formblok.quotation.data.model

import com.google.gson.annotations.SerializedName

data class BusinessRuleOutput(
    @SerializedName("driveway_single") var drivewaySingle: Double = 0.0,
    @SerializedName("driveway_double") var drivewayDouble: Double = 0.0,
    @SerializedName("driveway_multi") var drivewayMulti: Double = 0.0,
    @SerializedName("driveway_apartment") var drivewayApartment: Double = 0.0,
    @SerializedName("alfresco_single") var alfrescoSingle: Double = 0.0,
    @SerializedName("alfresco_double") var alfrescoDouble: Double = 0.0,
    @SerializedName("alfresco_multi") var alfrescoMulti: Double = 0.0,
    @SerializedName("alfresco_apartment") var alfrescoApartment: Double = 0.0,
    @SerializedName("traditional_setback") var traditionalSetback: Double = 0.0,
    @SerializedName("contemporary_setback") var comtemporarySetback: Double = 0.0,
    @SerializedName("north_setback") var northSetback: Double = 0.0
) {

}