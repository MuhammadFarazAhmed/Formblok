package com.incubasys.formblok.auth.callback

import com.incubasys.formblok.common.callback.FragmentCallback

interface UserTypeFragmentCallback : FragmentCallback {
    fun onUserTypeFragmentReady()
    fun onBuyerClicked()
    fun onAgentClicked()
}