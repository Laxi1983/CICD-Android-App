package com.undp.fleettracker.di.module

import com.undp.fleettracker.activity.auth.AuthActivity
import com.undp.fleettracker.activity.dashboard.DashboardActivity
import com.undp.fleettracker.di.module.auth.AuthModule
import com.undp.fleettracker.di.module.auth.AuthScope
import com.undp.fleettracker.di.module.auth.AuthViewModelsModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author: << sandip.mahajan >>
 */

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [
            (AuthViewModelsModule::class),
            (AuthModule::class)
        ]
    )
    abstract fun contributeAuthActivity(): AuthActivity

    abstract fun contributeDashboardActivity(): DashboardActivity
}