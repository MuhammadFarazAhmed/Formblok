package com.incubasys.formblok.onboard.di

import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.onboard.ui.fragment.OnboardingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OnboardingBuilderModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [OnboardingFragmentBuilderModule::class])
    abstract fun contributeOnboardingFragment(): OnboardingFragment
}