package com.incubasys.formblok.projects.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.di.scope.SubFragmentScope
import com.incubasys.formblok.di.scope.ViewModelKey
import com.incubasys.formblok.projects.ui.AreaFragment
import com.incubasys.formblok.projects.ui.DetailFragment
import com.incubasys.formblok.projects.ui.InfoFragment
import com.incubasys.formblok.projects.ui.QuoteFragment
import com.incubasys.formblok.projects.viewmodel.AddressDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class AddressDetailFragmentBuilderModule {

    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(AddressDetailViewModel::class)
    abstract fun contributeAddressDetailViewModel(addressDetailViewModel: AddressDetailViewModel): ViewModel

    @SubFragmentScope
    @ContributesAndroidInjector(modules = [InfoFragmentBuilderModule::class])
    abstract fun contributeInfoFragment(): InfoFragment

    @SubFragmentScope
    @ContributesAndroidInjector(modules = [DetailFragmentBuilderModule::class])
    abstract fun contributeDetailFragment(): DetailFragment

    @SubFragmentScope
    @ContributesAndroidInjector(modules = [AreaFragmentBuilderModule::class])
    abstract fun contributeAreaFragment(): AreaFragment

    @SubFragmentScope
    @ContributesAndroidInjector(modules = [QuoteFragmentBuilderModule::class])
    abstract fun contributeQuoteFragment(): QuoteFragment
}