package com.incubasys.formblok.settings.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.di.scope.ActivityScope
import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.di.scope.ViewModelKey
import com.incubasys.formblok.settings.ui.ChangeNewPasswordFragment
import com.incubasys.formblok.settings.ui.ChangeOldPasswordFragment
import com.incubasys.formblok.settings.ui.SettingsFragment
import com.incubasys.formblok.settings.viewmodel.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class SettingsBuilderModule {

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(SettingsViewModel::class)
    abstract fun contributeSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeChangeOldPasswordFragment(): ChangeOldPasswordFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeChangeNewPasswordFragment(): ChangeNewPasswordFragment

}