package com.incubasys.formblok.quotation.data

import com.incubasys.formblok.quotation.data.api.QuotationApi
import javax.inject.Inject

class QuotationRepository @Inject constructor(private val quotationApi: QuotationApi) {

    fun getMaterialData() = quotationApi.gETMaterialsMaterialData()

    fun getRooms() = quotationApi.getRooms()

    fun getBusinessRules() = quotationApi.getBusinessRules()
}