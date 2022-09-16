package com.incubasys.formblok.di.activitylevel


import com.incubasys.formblok.auth.di.AuthBuilderModule
import com.incubasys.formblok.auth.di.AuthModule
import com.incubasys.formblok.auth.ui.activity.AuthActivity
import com.incubasys.formblok.common.ui.MessageActivity
import com.incubasys.formblok.di.scope.ActivityScope
import com.incubasys.formblok.home.di.HomeBuilderModule
import com.incubasys.formblok.home.di.HomeModule
import com.incubasys.formblok.home.ui.HomeActivity
import com.incubasys.formblok.onboard.di.OnboardingBuilderModule
import com.incubasys.formblok.onboard.ui.activity.OnboardingActivity
import com.incubasys.formblok.profile.di.EditProfileBuilderModule
import com.incubasys.formblok.profile.di.EditProfileModule
import com.incubasys.formblok.profile.ui.EditProfileActivity
import com.incubasys.formblok.projects.di.AddressDetailBuilderModule
import com.incubasys.formblok.projects.di.CreateProjectBuilderModule
import com.incubasys.formblok.projects.di.PropertyBuilderModule
import com.incubasys.formblok.projects.ui.AddressDetailActivity
import com.incubasys.formblok.projects.ui.CreateProjectActivity
import com.incubasys.formblok.projects.ui.PropertyDetailActivity
import com.incubasys.formblok.quotation.di.CreateQuotationBuilderModule
import com.incubasys.formblok.quotation.di.QuotationModule
import com.incubasys.formblok.quotation.ui.CreateQuotationActivity
import com.incubasys.formblok.settings.di.SettingsBuilderModule
import com.incubasys.formblok.settings.ui.SettingsActivity
import com.incubasys.formblok.splash.di.SplashBuilderModule
import com.incubasys.formblok.splash.ui.activity.SplashActivity
import com.incubasys.formblok.terms.di.PrivacyBuilderModule
import com.incubasys.formblok.terms.di.PrivacyModule
import com.incubasys.formblok.terms.ui.PrivacyPolicyActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [SplashBuilderModule::class])
    abstract fun contributeSplashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [OnboardingBuilderModule::class])
    abstract fun contributeOnboardingActivity(): OnboardingActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [AuthModule::class, AuthBuilderModule::class])
    abstract fun contributeAuthActivity(): AuthActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeMessageActivity(): MessageActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [PrivacyModule::class, PrivacyBuilderModule::class])
    abstract fun contributePrivacyPolicyActivity(): PrivacyPolicyActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeModule::class, HomeBuilderModule::class])
    abstract fun contributeHomeActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [EditProfileModule::class, EditProfileBuilderModule::class])
    abstract fun contributeEditProfileActivity(): EditProfileActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [AuthModule::class, SettingsBuilderModule::class])
    abstract fun contributeSettingsActivity(): SettingsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [CreateProjectBuilderModule::class])
    abstract fun contributeCreateProjectActivity(): CreateProjectActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [PropertyBuilderModule::class])
    abstract fun contributeProjectDetailActivity(): PropertyDetailActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [AddressDetailBuilderModule::class])
    abstract fun contributeAddressDetailActivity(): AddressDetailActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [QuotationModule::class, CreateQuotationBuilderModule::class])
    abstract fun contributeCreateQuotationActivity(): CreateQuotationActivity

}