package com.harryio.fizz.network

import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

private const val BASE_API_URL = "https://api.themoviedb.org"

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ApiKey

@Qualifier
internal annotation class InternalApi

@Module
object NetworkModule {

    @Provides
    @Singleton
    @JvmStatic
    @InternalApi
    fun moshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    @JvmStatic
    @InternalApi
    fun okHttpClient(@ApiKey apiKey: String): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(ApiKeyInterceptor(apiKey))
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
            .addInterceptor(UrlSchemeInterceptor)
            .build()

    @Provides
    @Singleton
    @JvmStatic
    fun retrofit(
        @InternalApi okHttpClient: Lazy<OkHttpClient>,
        @InternalApi moshi: Moshi
    ): Retrofit =
        Retrofit.Builder().baseUrl(BASE_API_URL)
            .delegatingCallFactory(okHttpClient)
            .addCallAdapterFactory(ApiResponseAdapter(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
}