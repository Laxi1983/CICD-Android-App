package com.undp.fleettracker.di.module

import com.undp.fleettracker.constants.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * @author: << sandip.mahajan >>
 */

@Module
object NetworkModule {

    @Singleton
    @JvmStatic
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(headersInterceptor())
            .apply { readTimeout(30L, TimeUnit.SECONDS)
                connectTimeout(30L, TimeUnit.SECONDS)
                writeTimeout(30L, TimeUnit.SECONDS) }
            .build()
    }

    private fun headersInterceptor() = Interceptor { chain ->
        chain.proceed(
            chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .also {
                    it.addHeader("Authorization", "Bearer ")
                }
                .build()
        )
    }

    @Singleton
    @JvmStatic
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

//    @Singleton
//    @JvmStatic
//    @Provides
//    fun provideNonJsonRetrofitInstance(): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .build()
//    }


}