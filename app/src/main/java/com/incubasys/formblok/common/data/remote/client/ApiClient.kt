package com.incubasys.formblok.common.data.remote.client


import com.incubasys.formblok.common.data.model.AuthHeader
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

import javax.inject.Inject

class ApiClient @Inject
constructor(
    private val okHttpClient: OkHttpClient,
    private val retrofit: Retrofit,
    private val apiAuthorizations: Map<String, Interceptor>
) {


    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit
            .create(serviceClass)

    }

    /**
     * Helper method to configure the first api key found
     *
     * @param apiKey API key
     */
    private fun setApiKey(apiKey: String) {
        for (apiAuthorization in apiAuthorizations.values) {
            if (apiAuthorization is ApiKeyAuth) {
                apiAuthorization.apiKey = apiKey
                return
            }
        }
    }

    /**
     * Helper method to configure the sid & stoken
     *
     * @param sessionKeys
     */
    private fun setSessionKeys(sessionKeys: AuthHeader) {
        for (apiAuthorization in apiAuthorizations.values) {
            if (apiAuthorization is SessionAuth) {
                apiAuthorization.setAuthHeader(sessionKeys)
                return
            }
        }
    }


}

