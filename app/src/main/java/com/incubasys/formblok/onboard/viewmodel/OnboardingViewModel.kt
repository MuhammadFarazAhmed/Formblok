package com.incubasys.formblok.onboard.viewmodel

import android.app.Application
import com.incubasys.formblok.FormBlokApplication
import com.incubasys.formblok.R
import com.incubasys.formblok.common.data.repository.SharedPreferencesRepository
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.onboard.data.Onboarding
import com.incubasys.formblok.util.ParseErrors
import javax.inject.Inject

class OnboardingViewModel @Inject constructor(
    application: Application,
    private val parseError: ParseErrors,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : BaseViewModel(application, parseError) {

    private var shownFromSettings = false

    var list = mutableListOf<Onboarding>()

    fun setUpList() {
        list.add(
            0, Onboarding(
                R.drawable.onboarding1
                , getApplication<FormBlokApplication>().getString(R.string.search_addresses_label)
                , getApplication<FormBlokApplication>().getString(R.string.find_properties_label)
            )
        )
        list.add(
            1, Onboarding(
                R.drawable.onboarding2
                , getApplication<FormBlokApplication>().getString(R.string.organize_your_build_label)
                , getApplication<FormBlokApplication>().getString(R.string.get_a_quote_label)
            )
        )
        list.add(
            2, Onboarding(
                R.drawable.onboarding3
                , getApplication<FormBlokApplication>().getString(R.string.manage_project_label)
                , getApplication<FormBlokApplication>().getString(R.string.keep_track_label)
            )
        )
    }

    fun setIsOnboardingShown() {
        sharedPreferencesRepository.setIsOnboardingShown(true)
    }

    fun isShownFromSettings(): Boolean {
        return shownFromSettings
    }

    fun setShownFromSettings(shownFromSettings: Boolean) {
        this.shownFromSettings = shownFromSettings
    }

}
