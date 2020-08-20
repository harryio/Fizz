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

@Module
object NetworkModule {

    private lateinit var apiKey: String

    fun setup(apiKey: String) {
        this.apiKey = apiKey
    }

    private val moshi by lazy {
        Moshi.Builder().build()
    }

    private val okHttpClient = lazy {
        OkHttpClient.Builder().addInterceptor(ApiKeyInterceptor(apiKey))
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
            .addInterceptor(UrlSchemeInterceptor)
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_API_URL)
            .delegatingCallFactory(okHttpClient)
            .addCallAdapterFactory(ApiResponseAdapter(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}