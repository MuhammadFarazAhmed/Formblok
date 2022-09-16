package com.incubasys.formblok.projects.data.api

import com.incubasys.formblok.common.data.model.ProjectPage
import com.incubasys.formblok.projects.data.model.*
import io.reactivex.Observable
import retrofit2.http.*

interface ProjectApi {

    /**
     * Project List
     *
     *
     */
    @Headers("Content-Type:application/json")
    @GET("projects")
    fun getProjectList(
        @Query("page") page: Int = 1, @Query("timestamp") timestamp: String = "",@Query("query") query: String? = ""
    ): Observable<ProjectPage>

    /**
     * Create A Projecg
     *
     *
     */
    @Headers("Content-Type:application/json")
    @POST("projects")
    fun pOSTProject(
        @Body projectInput: ProjectInput
    ): Observable<Project>

    /**
     * Create A Projech
     *
     *
     */
    @Headers("Content-Type:application/json")
    @GET("properties/{id}")
    fun gETPropertyDetail(
        @Path("id") id: Int
    ): Observable<PropertyOutput>

    @Headers("Content-Type:application/json")
    @PUT("projects/{id}/add_property")
    fun pUTPropertyToProject(
        @Path("id") id: Int,  @retrofit2.http.Body propertyInput: PropertyInput
    ): Observable<Project>
}