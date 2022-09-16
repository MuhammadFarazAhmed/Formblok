package com.incubasys.formblok.quotation.di

import androidx.lifecycle.ViewModel
import com.incubasys.formblok.di.scope.ViewModelKey
import com.incubasys.formblok.projects.ui.RenovationDetailFragment
import com.incubasys.formblok.quotation.ui.*
import com.incubasys.formblok.quotation.viewmodel.CreateQuotationViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class CreateQuotationBuilderModule {

    @Binds
    @IntoMap
    @ViewModelKey(CreateQuotationViewModel::class)
    abstract fun contributeCreateQuotationViewModel(createQuotationViewModel: CreateQuotationViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributeBuildTypeFragment(): BuildTypeFragment

    @ContributesAndroidInjector
    abstract fun contributeRenovationDetailFragment(): RenovationDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeDevelopmentTypeFragment(): DevelopmentTypeFragment

    @ContributesAndroidInjector
    abstract fun contributeFloorFragment(): FloorFragment

    @ContributesAndroidInjector
    abstract fun contributeRoomsFragment(): RoomsFragment

    @ContributesAndroidInjector
    abstract fun contributeMaterialFragment(): MaterialFragment

    @ContributesAndroidInjector
    abstract fun contributeMaterialItemFragment(): MaterialItemFragment

    @ContributesAndroidInjector
    abstract fun contributeRoomDetailFragment(): RoomDetailFragment
}