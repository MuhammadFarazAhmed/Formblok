package com.incubasys.formblok.notification.data

import com.incubasys.formblok.common.data.repository.BaseRepository
import com.incubasys.formblok.notification.data.api.NotificationApi
import com.incubasys.formblok.notification.data.model.NotificationInput
import javax.inject.Inject

class NotificationRepository @Inject constructor(private val notificationApi: NotificationApi) : BaseRepository() {

    val notificationsApi:NotificationApi
    get() = notificationApi

    fun createNotification(notificationInput: NotificationInput) = notificationApi.pOSTNotification(notificationInput)

    fun markAllNotifications() = notificationApi.pUTMarkAllNotifications()



}