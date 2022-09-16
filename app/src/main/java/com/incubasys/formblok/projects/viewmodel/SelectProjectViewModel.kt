package com.incubasys.formblok.projects.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.incubasys.formblok.common.data.model.Listing
import com.incubasys.formblok.common.data.model.PageInfo
import com.incubasys.formblok.common.data.model.SimpleResponse
import com.incubasys.formblok.common.data.pagination.ProjectDataSource
import com.incubasys.formblok.common.data.pagination.ProjectDataSourceFactory
import com.incubasys.formblok.common.data.remote.ApiResponse
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.projects.data.ProjectsRepository
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.projects.data.model.ProjectOutput
import com.incubasys.formblok.projects.data.model.PropertyInput
import com.incubasys.formblok.projects.data.model.PropertyOutput
import com.incubasys.formblok.util.ParseErrors
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
import javax.inject.Inject

class SelectProjectViewModel @Inject constructor(
    application: Application,
    private val parseErrors: ParseErrors,
    private val projectsRepository: ProjectsRepository
) :
    BaseViewModel(application, parseErrors) {

    val apiResponse = MutableLiveData<ApiResponse<Project>>()

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

    fun fetchProjects() {
        listing = getProjectList()
    }

    fun retryPage() {
        if (listing?.pagedListLiveData?.value != null) {
            (listing!!.pagedListLiveData.value?.dataSource as ProjectDataSource).retryPagination()
        }
    }

    private fun getProjectList(): Listing<Project> {
        val venuesDataSourceFactory = ProjectDataSourceFactory(parseErrors, projectsRepository.getProjectApi())
        venuesDataSourceFactory.setParams(query)
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPrefetchDistance(1)
            .setInitialLoadSizeHint(10)
            .setPageSize(20).build()

        val pagedListLiveData = LivePagedListBuilder(venuesDataSourceFactory, pagedListConfig)
            .setFetchExecutor(Executors.newFixedThreadPool(5))
            .setInitialLoadKey(PageInfo(page = 1, timestamp = ""))
            .build()
        return Listing(
            pagedListLiveData,
            Transformations.switchMap(
                venuesDataSourceFactory.mutableLiveData
            ) { input -> input.initialLoadStateLiveData },
            Transformations.switchMap(
                venuesDataSourceFactory.mutableLiveData
            ) { input -> input.paginatedNetworkStateLiveData }
        )
    }

    fun addAPropertyToProject(id: Int, propertyInput: PropertyInput) {
        compositeDisposable.add(projectsRepository.addPropertyToProject(id, propertyInput)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                apiResponse.value = ApiResponse(ApiStatus.LOADING)
            }.subscribeBy(onNext = {
                apiResponse.value = ApiResponse(ApiStatus.SUCCESS,it)
            }, onError = {
                apiResponse.value = ApiResponse(ApiStatus.ERROR,parseErrors.interpretErrors(it))
            })
        )
    }

}