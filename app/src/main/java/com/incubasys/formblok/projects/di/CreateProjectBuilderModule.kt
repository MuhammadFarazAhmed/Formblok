package com.incubasys.formblok.projects.di

import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.projects.ui.CreateProjectFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CreateProjectBuilderModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [CreateProjectFragmentBuilderModule::class])
    abstract fun contributeCreateProjectFragment(): CreateProjectFragment
}