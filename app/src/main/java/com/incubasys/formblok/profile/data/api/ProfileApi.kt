package com.incubasys.formblok.profile.data.api

import com.incubasys.formblok.common.data.model.*
import com.incubasys.formblok.common.data.remote.ApiResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

    /**
     * Edit Profile
     *
     *
     */
    @Headers("Content-Type:application/json")
    @PUT("users/update_profile")
    fun pUtUpdateProfile(
        @retrofit2.http.Body body: ProfileInput
    ): Observable<User>


}