package com.incubasys.formblok.projects.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.di.scope.SubFragmentScope
import com.incubasys.formblok.di.scope.ViewModelKey
import com.incubasys.formblok.projects.viewmodel.AreaViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AreaFragmentBuilderModule {

    @Binds
    @IntoMap
    @SubFragmentScope
    @ViewModelKey(AreaViewModel::class)
    abstract fun contributeAreaViewModel(areaViewModel: AreaViewModel): ViewModel


}
