package com.incubasys.formblok.onboard.callback

interface OnboardingItemCallback {
    fun onNextButtonClicked(currentIndex: Int)
    fun onGetStartedButtonClicked()
}
