package com.incubasys.formblok.notification.viewmodel

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
import com.incubasys.formblok.common.data.pagination.NotificationDataSourceFactory
import com.incubasys.formblok.common.data.pagination.ProjectDataSource
import com.incubasys.formblok.common.data.remote.ApiResponse
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.notification.data.NotificationRepository
import com.incubasys.formblok.notification.data.model.NotificationInput
import com.incubasys.formblok.notification.data.model.NotificationOutput
import com.incubasys.formblok.util.ParseErrors
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
import javax.inject.Inject

class NotificationViewModel @Inject constructor(
    application: Application,
    private val parseErrors: ParseErrors,
    private val notificationRepository: NotificationRepository
) : BaseViewModel(application, parseErrors) {

    var apiResponse = MutableLiveData<ApiResponse<Any>>()
    var markNotificationResponse = MutableLiveData<ApiResponse<Any>>()
    var notificationInput = MutableLiveData<NotificationInput>()
    var notificationOutput = MutableLiveData<NotificationOutput>()

    val networkState = ObservableField(SimpleResponse(ApiStatus.LOADING))
    private var listing: Listing<NotificationOutput>? = null

    fun createNotification(notificationInput: NotificationInput) {
        compositeDisposable.add(notificationRepository.createNotification(notificationInput)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                apiResponse.value = ApiResponse(ApiStatus.LOADING)
            }.subscribeBy(onNext = {
                apiResponse.value = ApiResponse(ApiStatus.SUCCESS)
                notificationOutput.value = it
            }, onError = {
                apiResponse.value = ApiResponse(ApiStatus.ERROR, parseErrors.interpretErrors(it))
            })
        )
    }

    fun fetchNotification() {
        listing = getNotificationList()
    }

    val notifications: LiveData<PagedList<NotificationOutput>>
        get() = listing!!.pagedListLiveData

    val initProgress: LiveData<SimpleResponse>
        get() = listing!!.initLoadLiveData

    val pageProgress: LiveData<SimpleResponse>
        get() = listing!!.pageLoadLiveData

    fun refreshNotificationList() {
        if (listing?.pagedListLiveData?.value != null) {
            listing!!.pagedListLiveData.value?.dataSource?.invalidate()
        }
    }

    fun retryPage() {
        if (listing?.pagedListLiveData?.value != null) {
            (listing!!.pagedListLiveData.value?.dataSource as ProjectDataSource).retryPagination()
        }
    }

    private fun getNotificationList(): Listing<NotificationOutput> {
        val notificationDataSourceFactory =
            NotificationDataSourceFactory(parseErrors, notificationRepository.notificationsApi)
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPrefetchDistance(1)
            .setInitialLoadSizeHint(10)
            .setPageSize(20).build()

        val pagedListLiveData = LivePagedListBuilder(notificationDataSourceFactory, pagedListConfig)
            .setFetchExecutor(Executors.newFixedThreadPool(5))
            .setInitialLoadKey(PageInfo(page = 1, timestamp = ""))
            .build()
        return Listing(
            pagedListLiveData,
            Transformations.switchMap(
                notificationDataSourceFactory.mutableLiveData
            ) { input -> input.initialLoadStateLiveData },
            Transformations.switchMap(
                notificationDataSourceFactory.mutableLiveData
            ) { input -> input.paginatedNetworkStateLiveData }
        )
    }

    fun markAllNotifications(){
        compositeDisposable.add(notificationRepository.markAllNotifications()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                markNotificationResponse.value = ApiResponse(ApiStatus.LOADING)
            }.subscribeBy(onNext = {
                markNotificationResponse.value = ApiResponse(ApiStatus.SUCCESS)
                refreshNotificationList()
            }, onError = {
                markNotificationResponse.value = ApiResponse(ApiStatus.ERROR, parseErrors.interpretErrors(it))
            })
        )
    }

}
