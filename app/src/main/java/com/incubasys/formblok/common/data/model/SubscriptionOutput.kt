package com.incubasys.formblok.common.data.model

import org.joda.time.DateTime

class SubscriptionOutput(
    var id: Int? = null,
    var order_id: String? = null,
    var purchase_token: String? = null,
    var type: Int? = null,
    var valid_till: DateTime? = null
)