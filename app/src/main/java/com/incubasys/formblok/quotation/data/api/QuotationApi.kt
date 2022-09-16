package com.incubasys.formblok.quotation.data.api

import com.incubasys.formblok.common.data.model.MaterialData
import com.incubasys.formblok.common.data.model.RoomOutput
import com.incubasys.formblok.quotation.data.model.BusinessRuleOutput
import io.reactivex.Observable
import retrofit2.http.GET

interface QuotationApi {

    @GET("materials/material_data")
    fun gETMaterialsMaterialData(): Observable<List<MaterialData>>

    @GET("rooms")
    fun getRooms(): Observable<List<RoomOutput>>

    @GET("business_rules")
    fun getBusinessRules(): Observable<BusinessRuleOutput>
}