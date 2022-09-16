package com.incubasys.formblok.projects.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.di.scope.ViewModelKey
import com.incubasys.formblok.projects.viewmodel.PropertyListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PropertyListFragmentBuilderModule {

    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(PropertyListViewModel::class)
    abstract fun contributeProjectDetailViewModel(propertyListViewModel: PropertyListViewModel): ViewModel

}