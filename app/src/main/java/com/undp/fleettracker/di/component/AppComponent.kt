package com.undp.fleettracker.di.component

import android.app.Application
import com.undp.fleettracker.AFleetTracker
import com.undp.fleettracker.di.module.ActivityBuildersModule
import com.undp.fleettracker.di.module.AppModule
import com.undp.fleettracker.di.module.NetworkModule
import com.undp.fleettracker.di.module.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


/**
 * @author: << sandip.mahajan >>
 */
@Singleton
@Component(
    modules = [
        (AndroidInjectionModule::class),
        (ActivityBuildersModule::class),
        (NetworkModule::class),
        (AppModule::class),
        (ViewModelFactoryModule::class)
    ]
)
interface AppComponent : AndroidInjector<AFleetTracker> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}