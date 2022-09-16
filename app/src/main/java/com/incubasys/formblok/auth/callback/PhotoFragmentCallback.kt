package com.incubasys.formblok.auth.callback

import com.incubasys.formblok.common.callback.FragmentCallback

interface PhotoFragmentCallback : FragmentCallback {
    fun onPhotoFragmentReady()
    fun onChoosePhotoClicked()
}