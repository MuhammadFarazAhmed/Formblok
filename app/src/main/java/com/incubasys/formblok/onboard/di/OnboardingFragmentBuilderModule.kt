package com.incubasys.formblok.onboard.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.di.scope.ViewModelKey
import com.incubasys.formblok.onboard.viewmodel.OnboardingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OnboardingFragmentBuilderModule {
    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(OnboardingViewModel::class)
    abstract fun onboardingViewModel(onboardingViewModel: OnboardingViewModel): ViewModel
}
