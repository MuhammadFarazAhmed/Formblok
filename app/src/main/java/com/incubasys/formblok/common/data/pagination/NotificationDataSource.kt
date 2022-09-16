package com.incubasys.formblok.common.data.pagination

import com.incubasys.formblok.common.data.model.PageInfo
import com.incubasys.formblok.common.data.model.ProjectPage
import com.incubasys.formblok.notification.data.api.NotificationApi
import com.incubasys.formblok.notification.data.model.NotificationOutput
import com.incubasys.formblok.notification.data.model.NotificationPage
import com.incubasys.formblok.projects.data.api.ProjectApi
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.util.ParseErrors
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import java.util.*

class NotificationDataSource(override var parseErrors: ParseErrors, var notificationApi: NotificationApi) :
    BaseDataSource<NotificationOutput, NotificationApi>(parseErrors, notificationApi) {

    override fun getObservable(params: PageInfo): Observable<List<NotificationOutput>> {
        return api.getNotificationList(params.page, params.timestamp)
            .flatMap(Function<NotificationPage, ObservableSource<List<NotificationOutput>>> { t->
                if (t.notifications != null && t.notifications!!.isNotEmpty()) {
                    return@Function Observable.fromIterable(t.notifications)
                        .toList().toObservable()
                } else {
                    val items = ArrayList<NotificationOutput>()
                    return@Function Observable.just<List<NotificationOutput>>(items)
                }
            })
    }
}
