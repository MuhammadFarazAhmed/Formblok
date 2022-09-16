package com.incubasys.formblok.di.activitylevel;

import androidx.lifecycle.ViewModelProvider;
import com.incubasys.formblok.custom.ViewModelFactory;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelModule {
    // Here are the ViewModules used by any Module and Factory for all ViewModules

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);

}
