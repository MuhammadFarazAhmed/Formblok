package com.incubasys.formblok.projects.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.incubasys.formblok.common.data.model.Listing
import com.incubasys.formblok.common.data.model.PageInfo
import com.incubasys.formblok.common.data.model.SimpleResponse
import com.incubasys.formblok.common.data.pagination.ProjectDataSource
import com.incubasys.formblok.common.data.pagination.ProjectDataSourceFactory
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.projects.data.ProjectsRepository
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.util.ParseErrors
import java.util.concurrent.Executors
import javax.inject.Inject

class ProjectViewModel @Inject constructor(
    application: Application,
    private val parseErrors: ParseErrors,
    private val projectsRepository: ProjectsRepository
) :
    BaseViewModel(application, parseErrors) {

    val networkState = ObservableField(SimpleResponse(ApiStatus.LOADING))
    private var listing: Listing<Project>? = null
    var query: String? = null

    fun getProjects(): LiveData<PagedList<Project>> {
        return listing!!.pagedListLiveData
    }

    fun getInitProgress(): LiveData<SimpleResponse> {
        return listing!!.initLoadLiveData
    }

    fun getPageProgress(): LiveData<SimpleResponse> {
        return listing!!.pageLoadLiveData
    }

    fun refreshProjectList() {
        if (listing?.pagedListLiveData?.value != null) {
            listing!!.pagedListLiveData.value?.dataSource?.invalidate()
        }
    }

    fun retryPage() {
        if (listing?.pagedListLiveData?.value != null) {
            (listing!!.pagedListLiveData.value?.dataSource as ProjectDataSource).retryPagination()
        }
    }

    fun fetchProjects() {
        listing = getProjectList()
    }

    private fun getProjectList(): Listing<Project> {
        val projectDataSourceFactory = ProjectDataSourceFactory(parseErrors, projectsRepository.getProjectApi())
        projectDataSourceFactory.setParams(query)
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPrefetchDistance(1)
            .setInitialLoadSizeHint(10)
            .setPageSize(20).build()

        val pagedListLiveData = LivePagedListBuilder(projectDataSourceFactory, pagedListConfig)
            .setFetchExecutor(Executors.newFixedThreadPool(5))
            .setInitialLoadKey(PageInfo(page = 1, timestamp = ""))
            .build()
        return Listing(
            pagedListLiveData,
            switchMap(
                projectDataSourceFactory.mutableLiveData
            ) { input -> input.initialLoadStateLiveData },
            switchMap(
                projectDataSourceFactory.mutableLiveData
            ) { input -> input.paginatedNetworkStateLiveData }
        )
    }

}
