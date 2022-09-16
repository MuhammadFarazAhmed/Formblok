package com.incubasys.formblok.common.data.remote.client

import com.incubasys.formblok.common.data.model.AuthHeader
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class SessionAuth
@Inject constructor() : Interceptor {

    private var authHeader = AuthHeader()

    fun setAuthHeader(authHeader: AuthHeader) {
        this.authHeader = authHeader
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader(sidParamName, authHeader.sid.toString())
            .addHeader(stokenParamName, authHeader.stoken.toString())
            .build()
        return chain.proceed(request)
    }

    companion object {
        private const val location = "header"
        private const val sidParamName = "sid"
        private const val stokenParamName = "stoken"
    }
}
