package com.undp.fleettracker.di.module

import androidx.lifecycle.ViewModelProvider
import com.undp.fleettracker.viewmodels.ViewModelProvidersFactory
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * @author: << sandip.mahajan >>
 */

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelProvidersFactory?): ViewModelProvider.Factory?
}