/*
package com.incubasys.formblok.common.data.remote.api

import com.incubasys.formblok.common.data.model.Email
import com.incubasys.formblok.common.data.model.Message
import com.incubasys.formblok.common.data.model.SessionOutput
import retrofit2.Call
import retrofit2.http.*


interface DefaultApi {
    */
/**
     * Signout
     *
     * @param api  (required)
     * @param sid  (required)
     * @param stoken  (required)
     * @return Call&lt;Void&gt;
     *//*

    @DELETE("auth/signout")
    fun dELETEAuthSignout(
        @Header("api") api: String, @Header("sid") sid: String, @Header("stoken") stoken: String
    ): Call<Void>

    */
/**
     * Check Email Duplication
     *
     * @param api  (required)
     * @param body  (optional)
     * @return Call&lt;Message&gt;
     *//*

    @Headers("Content-Type:application/json")
    @GET("auth/check_email_duplication")
    fun gETAuthCheckEmailDuplication(
        @Header("api") api: String, @Body body: Email
    ): Call<Message>

    */
/**
     * Validate Session
     *
     * @param api  (required)
     * @param sid  (required)
     * @param stoken  (required)
     * @return Call&lt;SessionOutput&gt;
     *//*

    @GET("auth/validate_session")
    fun gETAuthValidateSession(
        @Header("api") api: String, @Header("sid") sid: String, @Header("stoken") stoken: String
    ): Call<SessionOutput>

    */
/**
     * Verify Email
     *
     * @param pin  (required)
     * @param api  (required)
     * @return Call&lt;Message&gt;
     *//*

    @GET("auth/verify_email")
    fun gETAuthVerifyEmail(
        @Path("pin") pin: String, @Header("api") api: String
    ): Call<Message>

    */
/**
     * Fetch All Material Categories
     *
     * @param api  (required)
     * @param sid  (required)
     * @param stoken  (required)
     * @return Call&lt;List&lt;MaterialCategoryOutput&gt;&gt;
     *//*

    @GET("materials/categories")
    fun gETMaterialsCategories(
        @Header("api") api: String, @Header("sid") sid: String, @Header("stoken") stoken: String
    ): Call<List<MaterialCategoryOutput>>

    */
/**
     * Fetch all Material Data in a Particular Category
     *
     * @param api  (required)
     * @param sid  (required)
     * @param stoken  (required)
     * @param categoryId  (optional)
     * @return Call&lt;List&lt;MaterialTypeProperty&gt;&gt;
     *//*

    @GET("materials/fetch_filtered")
    fun gETMaterialsFetchFiltered(
        @Header("api") api: String, @Header("sid") sid: String, @Header("stoken") stoken: String, @Path("category_id") categoryId: Int?
    ): Call<List<MaterialTypeProperty>>

    */
/**
     * Fetch All Construction Material Data
     *
     * @param api  (required)
     * @param sid  (required)
     * @param stoken  (required)
     * @return Call&lt;List&lt;MaterialData&gt;&gt;
     *//*

    @GET("materials/material_data")
    fun gETMaterialsMaterialData(
        @Header("api") api: String, @Header("sid") sid: String, @Header("stoken") stoken: String
    ): Call<List<MaterialData>>

    */
/**
     * List Projects
     *
     * @param api  (required)
     * @param sid  (required)
     * @param stoken  (required)
     * @param page  (optional)
     * @param timestamp  (optional)
     * @return Call&lt;Object&gt;
     *//*

    @GET("projects")
    fun gETProjects(
        @Header("api") api: String, @Header("sid") sid: String, @Header("stoken") stoken: String, @Path("page") page: Int?, @Path(
            "timestamp"
        ) timestamp: String
    ): Call<Any>

    */
/**
     * Get All Details of a Project
     *
     * @param id  (required)
     * @param api  (required)
     * @param sid  (required)
     * @param stoken  (required)
     * @return Call&lt;ProjectOutput&gt;
     *//*

    @GET("projects/{id}")
    fun gETProjectsId(
        @Path("id") id: String, @Header("api") api: String, @Header("sid") sid: String, @Header("stoken") stoken: String
    ): Call<ProjectOutput>

    */
/**
     * Get Details of a Property
     *
     * @param id  (required)
     * @param api  (required)
     * @param sid  (required)
     * @param stoken  (required)
     * @return Call&lt;PropertyOutput&gt;
     *//*

    @GET("properties/{id}")
    fun gETPropertiesId(
        @Path("id") id: String, @Header("api") api: String, @Header("sid") sid: String, @Header("stoken") stoken: String
    ): Call<PropertyOutput>

    */
/**
     * Fetch All Rooms
     *
     * @param api  (required)
     * @param sid  (required)
     * @param stoken  (required)
     * @return Call&lt;List&lt;RoomOutput&gt;&gt;
     *//*

    @GET("rooms")
    fun gETRooms(
        @Header("api") api: String, @Header("sid") sid: String, @Header("stoken") stoken: String
    ): Call<List<RoomOutput>>

    */
/**
     * Fetch Support Content
     *
     * @param api  (required)
     * @return Call&lt;SupportContentOutput&gt;
     *//*

    @GET("support_contents")
    fun gETSupportContents(
        @Header("api") api: String
    ): Call<SupportContentOutput>

    */
/**
     * Request Reset Password
     *
     * @param api  (required)
     * @param body  (optional)
     * @return Call&lt;Message&gt;
     *//*

    @Headers("Content-Type:application/json")
    @POST("auth/reset_password")
    fun pOSTAuthResetPassword(
        @Header("api") api: String, @Body body: Email
    ): Call<Message>

    */
/**
     * Signin
     *
     * @param api  (required)
     * @param body  (optional)
     * @return Call&lt;SessionOutput&gt;
     *//*

    @Headers("Content-Type:application/json")
    @POST("auth/signin")
    fun pOSTAuthSignin(
        @Header("api") api: String, @Body body: LoginInput
    ): Call<SessionOutput>

    */
/**
     * Signup
     *
     * @param api  (required)
     * @param body  (optional)
     * @return Call&lt;SessionOutput&gt;
     *//*

    @Headers("Content-Type:application/json")
    @POST("auth/signup")
    fun pOSTAuthSignup(
        @Header("api") api: String, @Body body: SignupInput
    ): Call<SessionOutput>

    */
/**
     * Submit Contact Form
     *
     * @param api  (required)
     * @param body  (optional)
     * @return Call&lt;Message&gt;
     *//*

    @Headers("Content-Type:application/json")
    @POST("contacts")
    fun pOSTContacts(
        @Header("api") api: String, @Body body: ContactInput
    ): Call<Message>

    */
/**
     * Create a Document
     *
     * @param api  (required)
     * @param body  (optional)
     * @return Call&lt;Document&gt;
     *//*

    @Headers("Content-Type:application/json")
    @POST("documents")
    fun pOSTDocuments(
        @Header("api") api: String, @Body body: DocumentInput
    ): Call<Document>

    */
/**
     * Create New Project
     *
     * @param body  (optional)
     * @return Call&lt;ProjectOutput&gt;
     *//*

    @Headers("Content-Type:application/json")
    @POST("projects")
    fun pOSTProjects(
        @Body body: ProjectInput
    ): Call<ProjectOutput>

    */
/**
     * Change Password
     *
     * @param api  (required)
     * @param sid  (required)
     * @param stoken  (required)
     * @param body  (optional)
     * @return Call&lt;Message&gt;
     *//*

    @Headers("Content-Type:application/json")
    @PUT("auth/change_password")
    fun pUTAuthChangePassword(
        @Header("api") api: String, @Header("sid") sid: String, @Header("stoken") stoken: String, @Body body: Any
    ): Call<Message>

    */
/**
     * Reset Password
     *
     * @param api  (required)
     * @param body  (optional)
     * @return Call&lt;Message&gt;
     *//*

    @Headers("Content-Type:application/json")
    @PUT("auth/reset_password")
    fun pUTAuthResetPassword(
        @Header("api") api: String, @Body body: ResetPasswordInput
    ): Call<Message>

    */
/**
     * Add a Property to an Address
     *
     * @param api  (required)
     * @param sid  (required)
     * @param stoken  (required)
     * @param id  (required)
     * @param body  (optional)
     * @return Call&lt;EmailVerificationInput&gt;
     *//*

    @Headers("Content-Type:application/json")
    @PUT("projects/{id}/add_property")
    fun pUTProjectsIdAddProperty(
        @Header("api") api: String, @Header("sid") sid: String, @Header("stoken") stoken: String, @Path("id") id: String, @Body body: PropertyInput
    ): Call<EmailVerificationInput>

    */
/**
     * Update User Profile
     *
     * @param api  (required)
     * @param sid  (required)
     * @param stoken  (required)
     * @param body  (optional)
     * @return Call&lt;User&gt;
     *//*

    @Headers("Content-Type:application/json")
    @PUT("users/update_profile")
    fun pUTUsersUpdateProfile(
        @Header("api") api: String, @Header("sid") sid: String, @Header("stoken") stoken: String, @Body body: ProfileInput
    ): Call<User>

}
*/
