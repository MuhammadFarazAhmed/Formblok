package com.incubasys.formblok.terms.data

import com.incubasys.formblok.terms.data.api.PrivacyApi
import javax.inject.Inject


class PrivacyPolicyRepository @Inject constructor(private val privacyApi: PrivacyApi) {

    fun getSupportContent() = privacyApi.getSupportContent()
}