package com.incubasys.formblok.common.data.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.incubasys.formblok.common.data.model.PageInfo
import com.incubasys.formblok.projects.data.api.ProjectApi
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.util.ParseErrors

class ProjectDataSourceFactory(override var parseErrors: ParseErrors, var projectApi: ProjectApi) :
    BaseDataSourceFactory<Project, ProjectApi>(parseErrors, projectApi) {

    private var query: String? = null

    val mutableLiveData: MutableLiveData<ProjectDataSource> = MutableLiveData()
    private var projectDataSource: ProjectDataSource? = null

    fun setParams(query: String?) {
        this.query = query
    }

    override fun create(): DataSource<PageInfo, Project> {
        projectDataSource = ProjectDataSource(parseErrors,projectApi)
        projectDataSource?.setParams(query)
        mutableLiveData.postValue(projectDataSource)
        return projectDataSource as ProjectDataSource
    }

}
