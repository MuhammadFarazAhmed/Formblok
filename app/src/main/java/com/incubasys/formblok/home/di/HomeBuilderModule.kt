package com.incubasys.formblok.home.di

import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.explore.ui.ExploreFragment
import com.incubasys.formblok.explore.di.ExploreFragmentBuilderModule
import com.incubasys.formblok.notification.di.NotificationFragmentBuilderModule
import com.incubasys.formblok.notification.ui.NotificationFragment
import com.incubasys.formblok.profile.di.ProfileFragmentBuilderModule
import com.incubasys.formblok.profile.ui.ProfileFragment
import com.incubasys.formblok.projects.di.PropertyListFragmentBuilderModule
import com.incubasys.formblok.projects.ui.ProjectFragment
import com.incubasys.formblok.projects.di.ProjectFragmentBuilderModule
import com.incubasys.formblok.projects.ui.PropertyListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeBuilderModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [ProjectFragmentBuilderModule::class])
    abstract fun contributeProjectFragment(): ProjectFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [ExploreFragmentBuilderModule::class])
    abstract fun contributeExploreFragment(): ExploreFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [NotificationFragmentBuilderModule::class])
    abstract fun contributeNotificationFragment(): NotificationFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [ProfileFragmentBuilderModule::class])
    abstract fun contributeProfileFragment(): ProfileFragment

}