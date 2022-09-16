package com.incubasys.formblok.auth.data.api

import com.incubasys.formblok.common.data.model.*
import com.incubasys.formblok.settings.data.ChangePasswordInput
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    /**
     * Check com.incubasys.formblok.common.data.model.Email Duplication
     *
     * @param api  (required)
     * @param body  (optional)
     * @return Call&lt;com.incubasys.formblok.common.data.model.Message&gt;
     */
    @Headers("Content-Type:application/json")
    @GET("auth/check_email_duplication")
    fun gETAuthCheckEmailDuplication(
        @Query("email") email: String
    ): Observable<Response<Message>>


    /**
     * Signup
     *
     * @param api  (required)
     * @param body  (optional)
     * @return Call&lt;com.incubasys.formblok.common.data.model.SessionOutput&gt;
     */
    @Headers("Content-Type:application/json")
    @POST("auth/signup")
    fun pOSTAuthSignup(
        @retrofit2.http.Body body: SignUpInput
    ): Observable<SessionOutput>

    /**
     * Signin
     *
     * @param api  (required)
     * @param body  (optional)
     * @return Call&lt;com.incubasys.formblok.common.data.model.SessionOutput&gt;
     */
    @Headers("Content-Type:application/json")
    @POST("auth/signin")
    fun pOSTAuthSignin(
        @retrofit2.http.Body body: LoginInput
    ): Observable<SessionOutput>

    /**
     * Request Reset Password
     *
     * @param api  (required)
     * @param body  (optional)
     * @return Call&lt;com.incubasys.formblok.common.data.model.Message&gt;
     */
    @Headers("Content-Type:application/json")
    @POST("auth/reset_password")
    fun pOSTAuthResetPassword(
        @retrofit2.http.Body body: Email
    ): Observable<Message>

    /**
     * Reset Password
     *
     * @param api  (required)
     * @param body  (optional)
     * @return Call&lt;com.incubasys.formblok.common.data.model.Message&gt;
     */
    @Headers("Content-Type:application/json")
    @PUT("auth/reset_password")
    fun pUTAuthResetPassword(
        @retrofit2.http.Body body: ResetPasswordInput
    ): Observable<Message>

    @Headers("Content-Type:application/json")
    @DELETE("auth/signout")
    fun logout(): Completable

    @Headers("Content-Type:application/json")
    @PUT("auth/change_password")
    fun changePassword(
        @retrofit2.http.Body body: ChangePasswordInput
    ): Observable<Message>
}