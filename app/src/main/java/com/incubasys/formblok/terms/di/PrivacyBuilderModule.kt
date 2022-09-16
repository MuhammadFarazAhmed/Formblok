package com.incubasys.formblok.terms.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.terms.ui.LegalFragment
import com.incubasys.formblok.terms.ui.LegalViewPagerFragment
import com.incubasys.formblok.terms.ui.PrivacyPolicyFragment
import com.incubasys.formblok.terms.viewmodel.PrivacyViewModel
import com.incubasys.formblok.di.scope.ActivityScope
import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.di.scope.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class PrivacyBuilderModule {

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(PrivacyViewModel::class)
    abstract fun privacyViewModel(privacyViewModel: PrivacyViewModel): ViewModel

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributePrivacyFragment(): PrivacyPolicyFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeLegalFragment(): LegalFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeLegalViewPagerFragment(): LegalViewPagerFragment

}