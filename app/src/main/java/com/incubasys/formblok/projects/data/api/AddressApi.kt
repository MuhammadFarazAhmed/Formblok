package com.incubasys.formblok.projects.data.api

import com.incubasys.formblok.common.data.model.Message
import com.incubasys.formblok.common.data.model.SubscriptionInput
import com.incubasys.formblok.common.data.model.SubscriptionOutput
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.projects.data.model.PropertyInput
import com.incubasys.formblok.projects.data.model.PropertyOutput
import io.reactivex.Observable
import retrofit2.http.*

interface AddressApi {

    @GET("addresses/{id}")
    fun getAddressDetail(
        @Path("id") addressId: String
    ): Observable<PropertyOutput>

    @Headers("Content-Type:application/json")
    @POST("users/subscribe")
    fun pOSTSubscription(
        @Body subscriptionInput: SubscriptionInput
    ): Observable<SubscriptionOutput>

    @Headers("Content-Type:application/json")
    @GET("users/check_status")
    fun checkSubscription(): Observable<Message>

    @Headers("Content-Type:application/json")
    @PUT("projects/{id}/add_property")
    fun pUTPropertyToProject(
        @Path("id") id: Int,  @retrofit2.http.Body propertyInput: PropertyInput
    ): Observable<Project>
}
