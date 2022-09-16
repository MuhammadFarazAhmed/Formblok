package com.incubasys.formblok.settings.callback

import com.incubasys.formblok.common.callback.FragmentBackCallback
import com.incubasys.formblok.common.callback.FragmentNextButtonCallback

interface ChangeOldPasswordFragmentCallback: FragmentBackCallback {

    fun onChangeOldPasswordNextClicked()

}