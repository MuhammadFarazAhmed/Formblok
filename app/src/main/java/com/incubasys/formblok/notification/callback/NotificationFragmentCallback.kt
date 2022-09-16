package com.incubasys.formblok.notification.callback

import com.incubasys.formblok.notification.data.model.NotificationOutput

interface NotificationFragmentCallback {

    val markAllAsUnReadLabelClicked: () -> Unit

    fun onNotificationClicked(notificationOutput: NotificationOutput)
}