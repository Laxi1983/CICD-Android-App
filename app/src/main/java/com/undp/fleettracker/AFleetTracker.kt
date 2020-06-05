package com.undp.fleettracker

import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.undp.fleettracker.constants.APP_CENTER_SECRET_KEY
import com.undp.fleettracker.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * @author: << sandip.mahajan >>
 */

class AFleetTracker : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        initCrashlytics()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build();
    }

    private fun initCrashlytics() {
        AppCenter.start(
            this, APP_CENTER_SECRET_KEY,
            Analytics::class.java, Crashes::class.java
        )
    }
}