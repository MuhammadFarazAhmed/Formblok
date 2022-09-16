package com.incubasys.formblok.splash.callback

interface SplashFragmentCallback {
    fun startHomeActivity(isOnboardingShown:Boolean,isLoggedIn :Boolean)
    fun startAuthActivity()
}