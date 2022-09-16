package com.incubasys.formblok.projects.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.di.scope.SubFragmentScope
import com.incubasys.formblok.di.scope.ViewModelKey
import com.incubasys.formblok.projects.viewmodel.InfoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class InfoFragmentBuilderModule {

    @Binds
    @IntoMap
    @SubFragmentScope
    @ViewModelKey(InfoViewModel::class)
    abstract fun contributeInfoViewModel(infoViewModel: InfoViewModel): ViewModel


}
