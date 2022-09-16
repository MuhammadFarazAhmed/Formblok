package com.incubasys.formblok.auth.callback

import com.incubasys.formblok.common.callback.FragmentCallback

interface ForgotPasswordCallback : FragmentCallback {
    fun onForgotPasswordFragmentReady()
}