package com.incubasys.formblok.projects.di

import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.projects.ui.AddressDetailFragment
import com.incubasys.formblok.projects.ui.SelectProjectFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AddressDetailBuilderModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [AddressDetailFragmentBuilderModule::class])
    abstract fun contributeAddressDetailFragment(): AddressDetailFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [SelectProjectFragmentBuilderModule::class])
    abstract fun contributeSelectProjectFragment(): SelectProjectFragment
}