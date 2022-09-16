package com.incubasys.formblok.terms.data.api

import com.incubasys.formblok.common.data.model.SupportContentOutput
import io.reactivex.Observable
import retrofit2.http.GET

interface PrivacyApi {

    @GET("support_contents")
    fun getSupportContent() : Observable<SupportContentOutput>
}