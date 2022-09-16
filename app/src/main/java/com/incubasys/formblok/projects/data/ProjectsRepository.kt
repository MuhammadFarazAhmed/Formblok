package com.incubasys.formblok.projects.data

import com.incubasys.formblok.common.data.repository.BaseRepository
import com.incubasys.formblok.projects.data.api.ProjectApi
import com.incubasys.formblok.projects.data.model.ProjectInput
import com.incubasys.formblok.projects.data.model.PropertyInput
import javax.inject.Inject

class ProjectsRepository @Inject constructor(private val projectApi: ProjectApi) : BaseRepository() {

    fun getProjectApi() = projectApi

    fun createAProject(projectInput: ProjectInput) = projectApi.pOSTProject(projectInput)

    fun getPropertyDetail(id: Int) = projectApi.gETPropertyDetail(id)

    fun addPropertyToProject(id: Int, propertyInput: PropertyInput)
            = projectApi.pUTPropertyToProject(id, propertyInput)

}