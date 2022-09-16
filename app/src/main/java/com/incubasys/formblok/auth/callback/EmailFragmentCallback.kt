package com.incubasys.formblok.auth.callback

import com.incubasys.formblok.common.callback.FragmentCallback

interface EmailFragmentCallback : FragmentCallback {
    fun onEmailFragmentReady()

}