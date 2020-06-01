package com.undp.fleettracker.di.module.auth

import androidx.lifecycle.ViewModel
import com.undp.fleettracker.di.model.ViewModelKey
import com.undp.fleettracker.viewmodels.auth.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author: << sandip.mahajan >>
 */

@Module
abstract class AuthViewModelsModule {

    @AuthScope
    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(viewModel: AuthViewModel?): ViewModel?
}