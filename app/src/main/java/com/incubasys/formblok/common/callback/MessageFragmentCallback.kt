package com.incubasys.formblok.common.callback

import com.incubasys.formblok.common.ui.MessageAction

interface MessageFragmentCallback {
    fun onMessageButtonClick(messageAction: MessageAction)
}