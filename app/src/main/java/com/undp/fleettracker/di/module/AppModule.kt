package com.undp.fleettracker.di.module

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.undp.fleettracker.R
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author: << sandip.mahajan >>
 */

@Module()
object AppModule {

    @Singleton
    @Provides
    @JvmStatic
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
    }

    @Singleton
    @Provides
    @JvmStatic
    fun provideGlideInstance(
        application: Application,
        requestOptions: RequestOptions
    ): RequestManager {
        return Glide
            .with(application)
            .setDefaultRequestOptions(requestOptions)
    }
}