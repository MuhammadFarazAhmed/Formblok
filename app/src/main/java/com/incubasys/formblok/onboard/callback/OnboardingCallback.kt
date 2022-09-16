package com.incubasys.formblok.onboard.callback

import androidx.viewpager.widget.ViewPager

interface OnboardingCallback {
        fun onGetStartedButtonClicked(isShownFromSettings:Boolean)
        fun onFragmentReady()
}
