package com.incubasys.formblok.splash.di

import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.splash.ui.fragment.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SplashBuilderModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [SplashFragmentBuilderModule::class])
    abstract fun contribteSplashFragment(): SplashFragment
}