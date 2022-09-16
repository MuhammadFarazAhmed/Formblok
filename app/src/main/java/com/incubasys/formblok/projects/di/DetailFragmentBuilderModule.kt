package com.incubasys.formblok.projects.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.di.scope.SubFragmentScope
import com.incubasys.formblok.di.scope.ViewModelKey
import com.incubasys.formblok.projects.viewmodel.DetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DetailFragmentBuilderModule {

    @Binds
    @IntoMap
    @SubFragmentScope
    @ViewModelKey(DetailViewModel::class)
    abstract fun contributeDetailViewModel(detailViewModel: DetailViewModel): ViewModel


}
