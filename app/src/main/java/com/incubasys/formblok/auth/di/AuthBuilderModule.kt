package com.incubasys.formblok.auth.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.auth.ui.fragment.*
import com.incubasys.formblok.auth.viewmodel.AuthViewModel
import com.incubasys.formblok.di.scope.ActivityScope
import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.di.scope.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class AuthBuilderModule {

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(AuthViewModel::class)
    abstract fun authViewModel(authViewModel: AuthViewModel): ViewModel

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeEmailFragment(): EmailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributePasswordFragment(): PasswordFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeForgotPasswordFragment(): ForgotFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeResetPasswordFragment(): ResetPasswordFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeCreatePasswordFragment(): CreatePasswordFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeUserTypeFragment(): UserTypeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeNameFragment(): NameFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributePhotoFragment(): PhotoFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeCropFragment(): CropFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeDobFragment(): DobFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeGenderFragment(): GenderFragment
}