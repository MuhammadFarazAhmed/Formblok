package com.incubasys.formblok.common.data.model


data class SubscriptionInput(
    var product_id: String? = null,
    var purchase_token: String? = null,
    var subscription_type: Int = 0
)