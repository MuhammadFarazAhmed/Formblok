
package com.incubasys.formblok.explore.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.di.scope.ViewModelKey
import com.incubasys.formblok.explore.viewmodel.ExploreViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ExploreFragmentBuilderModule {

    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(ExploreViewModel::class)
    abstract fun exploreViewModel(exploreViewModel: ExploreViewModel): ViewModel
}