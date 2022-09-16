package com.incubasys.formblok.projects.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.di.scope.SubFragmentScope
import com.incubasys.formblok.di.scope.ViewModelKey
import com.incubasys.formblok.projects.viewmodel.AreaViewModel
import com.incubasys.formblok.projects.viewmodel.QuoteViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class QuoteFragmentBuilderModule {

    @Binds
    @IntoMap
    @SubFragmentScope
    @ViewModelKey(QuoteViewModel::class)
    abstract fun contributeQuoteViewModel(quoteViewModel: QuoteViewModel): ViewModel


}
