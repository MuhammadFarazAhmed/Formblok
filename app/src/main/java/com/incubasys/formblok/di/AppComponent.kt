package com.incubasys.formblok.di

import android.app.Application
import com.incubasys.formblok.FormBlokApplication
import com.incubasys.formblok.di.activitylevel.ActivityBuilderModule
import com.incubasys.formblok.di.scope.AppScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import okhttp3.OkHttpClient

@AppScope
@Component(modules = [AndroidSupportInjectionModule::class, ActivityBuilderModule::class, AppModule::class])
interface AppComponent : AndroidInjector<FormBlokApplication> {

    override fun inject(instance: FormBlokApplication)

    //UserRepository userRepository();

    fun okhttpClient(): OkHttpClient

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}
