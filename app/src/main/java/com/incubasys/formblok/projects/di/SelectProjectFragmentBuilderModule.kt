package com.incubasys.formblok.projects.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.di.scope.ViewModelKey
import com.incubasys.formblok.projects.viewmodel.AddressDetailViewModel
import com.incubasys.formblok.projects.viewmodel.SelectProjectViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SelectProjectFragmentBuilderModule {

    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(SelectProjectViewModel::class)
    abstract fun contributeSelectProjectViewModel(selectProjectViewModel: SelectProjectViewModel): ViewModel
}