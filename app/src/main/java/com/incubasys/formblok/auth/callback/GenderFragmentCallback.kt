package com.incubasys.formblok.auth.callback

import com.incubasys.formblok.common.callback.FragmentCallback

interface GenderFragmentCallback : FragmentCallback {

    fun onGenderFragmentReady()
    fun onFemaleClicked()
    fun onMaleClicked()
}