package com.incubasys.formblok.common.data.remote.api

import com.incubasys.formblok.common.data.model.*
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    /**
     * Signout
     *
     * @param api  (required)
     * @param sid  (required)
     * @param stoken  (required)
     * @return Call&lt;Void&gt;
     */
    @DELETE("auth/signout")
    fun dELETEAuthSignout(
        @retrofit2.http.Header("api") api: String, @retrofit2.http.Path("sid") sid: String, @retrofit2.http.Path("stoken") stoken: String
    ): Call<Void>

    /**
     * Check com.incubasys.formblok.common.data.model.Valdiate Session
     *
     * @param api  (required)
     * @param body  (optional)
     * @return Call&lt;com.incubasys.formblok.common.data.model.Message&gt;
     */
    @Headers("Content-Type:application/json")
    @GET("auth/validate_session")
    fun gETAuthValidateSession(): Observable<SessionOutput>



    /**
     * Update User Profile
     *
     * @param api  (required)
     * @param sid  (required)
     * @param stoken  (required)
     * @param body  (optional)
     * @return Call&lt;User&gt;
     */
    @Headers("Content-Type:application/json")
    @PUT("users/update_profile")
    fun pUTUsersUpdateProfile(
        @retrofit2.http.Header("api") api: String, @retrofit2.http.Path("sid") sid: String, @retrofit2.http.Path("stoken") stoken: String, @retrofit2.http.Body body: ProfileInput
    ): Call<User>

}
