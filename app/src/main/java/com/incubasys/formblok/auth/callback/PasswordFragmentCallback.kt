package com.incubasys.formblok.auth.callback

import com.incubasys.formblok.common.callback.FragmentCallback

interface PasswordFragmentCallback : FragmentCallback {
    fun onPasswordFragmentReady()
}