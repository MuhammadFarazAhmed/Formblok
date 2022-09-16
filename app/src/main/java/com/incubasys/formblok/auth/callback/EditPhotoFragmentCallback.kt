package com.incubasys.formblok.auth.callback

import com.incubasys.formblok.common.callback.FragmentBackCallback

interface EditPhotoFragmentCallback : FragmentBackCallback {

    fun onEditPhotoBackPress()
    fun onEditPhotoChangePhotoClicked()

}