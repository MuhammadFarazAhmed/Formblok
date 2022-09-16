package com.incubasys.formblok.common.data.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.incubasys.formblok.common.data.model.PageInfo
import com.incubasys.formblok.notification.data.api.NotificationApi
import com.incubasys.formblok.notification.data.model.NotificationOutput
import com.incubasys.formblok.projects.data.api.ProjectApi
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.util.ParseErrors

class NotificationDataSourceFactory(override var parseErrors: ParseErrors, var notificationApi: NotificationApi) :
    BaseDataSourceFactory<NotificationOutput, NotificationApi>(parseErrors, notificationApi) {

    val mutableLiveData: MutableLiveData<NotificationDataSource> = MutableLiveData()
    private var notificationDataSource: NotificationDataSource? = null

    override fun create(): DataSource<PageInfo, NotificationOutput> {
        notificationDataSource = NotificationDataSource(parseErrors,notificationApi)
        mutableLiveData.postValue(notificationDataSource)
        return notificationDataSource as NotificationDataSource
    }

}
