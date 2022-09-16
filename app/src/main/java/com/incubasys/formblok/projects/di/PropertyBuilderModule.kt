package com.incubasys.formblok.projects.di

import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.projects.ui.NoAddressFragment
import com.incubasys.formblok.projects.ui.PropertyDetailFragment
import com.incubasys.formblok.projects.ui.PropertyListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PropertyBuilderModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeNoAddressFragment(): NoAddressFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [PropertyListFragmentBuilderModule::class])
    abstract fun contributeProjectDetailFragment(): PropertyListFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [PropertyDetailFragmentBuilderModule::class])
    abstract fun contributePropertyDetailFragment(): PropertyDetailFragment
}