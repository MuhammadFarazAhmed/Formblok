package com.incubasys.formblok.explore.data.api

import com.incubasys.formblok.common.data.model.SubscriptionInput
import com.incubasys.formblok.common.data.model.SubscriptionOutput
import com.incubasys.formblok.explore.data.model.AddressShort
import io.reactivex.Observable
import retrofit2.http.*

interface ExploreApi {

    @Headers("Content-Type:application/json")
    @GET("addresses")
    fun fetchAllAddressesInaArea(
        @Query("lat") lat: Double, @Query("lng") lng: Double
    ): Observable<List<AddressShort>>

    /*@Headers("Content-Type:application/json")
    @POST("users/subscribe")
    fun pOSTSubscription(
        @Body subscriptionInput: SubscriptionInput
    ): Observable<SubscriptionOutput>*/
}