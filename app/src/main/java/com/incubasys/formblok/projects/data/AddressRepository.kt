package com.incubasys.formblok.projects.data

import com.incubasys.formblok.common.data.model.SubscriptionInput
import com.incubasys.formblok.common.data.repository.BaseRepository
import com.incubasys.formblok.projects.data.api.AddressApi
import com.incubasys.formblok.projects.data.model.PropertyInput
import javax.inject.Inject

class AddressRepository @Inject constructor(private val addressApi: AddressApi) : BaseRepository() {

    fun getAddressDetail(id: String) = addressApi.getAddressDetail(id)

    fun getSubscription() = addressApi.checkSubscription()

    fun pOSTSubscription(subscriptionInput: SubscriptionInput) =
        addressApi.pOSTSubscription(subscriptionInput)

    fun addPropertyToProject(id: Int, propertyInput: PropertyInput)
            = addressApi.pUTPropertyToProject(id, propertyInput)
}