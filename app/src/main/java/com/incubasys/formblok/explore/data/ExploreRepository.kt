package com.incubasys.formblok.explore.data

import com.google.android.gms.maps.model.LatLng
import com.incubasys.formblok.common.data.model.SubscriptionInput
import com.incubasys.formblok.common.data.repository.BaseRepository
import com.incubasys.formblok.explore.data.api.ExploreApi
import javax.inject.Inject

class ExploreRepository @Inject constructor(private val exploreApi: ExploreApi) : BaseRepository() {

    fun fetchAddressesForaArea(latLng: LatLng) = exploreApi.fetchAllAddressesInaArea(latLng.latitude, latLng.longitude)

   /* fun pOSTSubscription(subscriptionInput: SubscriptionInput) = exploreApi.pOSTSubscription(subscriptionInput)*/
}