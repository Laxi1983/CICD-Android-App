package com.undp.fleettracker.di.module.auth

import com.undp.fleettracker.network.api.auth.AuthAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * @author: << sandip.mahajan >>
 */

@Module
object AuthModule {

    @AuthScope
    @JvmStatic
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthAPI {
        return retrofit.create(AuthAPI::class.java)
    }
}