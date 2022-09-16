package com.incubasys.formblok.projects.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.di.scope.ViewModelKey
import com.incubasys.formblok.projects.viewmodel.CreateProjectViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CreateProjectFragmentBuilderModule {

    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(CreateProjectViewModel::class)
    abstract fun contributeCreateProjectViewModel(createProjectViewModel: CreateProjectViewModel): ViewModel
}