package com.incubasys.formblok.notification.data.api

import com.incubasys.formblok.notification.data.model.NotificationInput
import com.incubasys.formblok.notification.data.model.NotificationOutput
import com.incubasys.formblok.notification.data.model.NotificationPage
import io.reactivex.Observable
import retrofit2.http.*

interface NotificationApi {

    @Headers("Content-Type:application/json")
    @POST("notifications")
    fun pOSTNotification(@Body notificationInput: NotificationInput):
            Observable<NotificationOutput>

    /**
     * Project List
     *
     *
     */
    @Headers("Content-Type:application/json")
    @GET("notifications")
    fun getNotificationList(
        @Query("page") page: Int = 1, @Query("timestamp") timestamp: String = ""
    ): Observable<NotificationPage>


    @Headers("Content-Type:application/json")
    @PUT("notifications/mark_read")
    fun pUTMarkAllNotifications():
            Observable<String>

}