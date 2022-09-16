package com.incubasys.formblok.common.callback

import com.incubasys.formblok.auth.ui.activity.GotoFragment

interface FragmentCallback {
    fun onNextButtonClicked(gotoFragment: GotoFragment)
}