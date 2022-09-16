package com.incubasys.formblok.profile.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.di.scope.ViewModelKey
import com.incubasys.formblok.profile.viewmodel.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProfileFragmentBuilderModule {

    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(ProfileViewModel::class)
    abstract fun profileViewModel(profileViewModel: ProfileViewModel): ViewModel
}