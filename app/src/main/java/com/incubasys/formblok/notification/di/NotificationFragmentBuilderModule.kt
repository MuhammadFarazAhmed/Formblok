package com.incubasys.formblok.notification.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.di.scope.ViewModelKey
import com.incubasys.formblok.notification.viewmodel.NotificationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class NotificationFragmentBuilderModule {

    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(NotificationViewModel::class)
    abstract fun notificationViewModel(notificationViewModel: NotificationViewModel): ViewModel
}