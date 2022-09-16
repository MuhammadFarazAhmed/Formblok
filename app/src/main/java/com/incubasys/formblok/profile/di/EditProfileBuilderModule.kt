package com.incubasys.formblok.profile.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.di.scope.ActivityScope
import com.incubasys.formblok.di.scope.FragmentScope
import com.incubasys.formblok.di.scope.ViewModelKey
import com.incubasys.formblok.profile.ui.*
import com.incubasys.formblok.profile.viewmodel.EditProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class EditProfileBuilderModule {

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(EditProfileViewModel::class)
    abstract fun contributeEditProfileViewModel(editProfileViewModel: EditProfileViewModel): ViewModel

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeEditProfileFragment(): EditProfileFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeEditCropFragment(): EditCropFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeEditPhotoFragment(): EditPhotoFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeEditEmailFragment(): EditEmailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeEditNameFragment(): EditNameFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeEditGenderFragment(): EditGenderFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeEditDobFragment(): EditDobFragment
}