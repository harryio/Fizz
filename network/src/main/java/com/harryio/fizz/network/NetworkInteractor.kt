package com.harryio.fizz.network

import com.squareup.moshi.Moshi
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_API_URL = "https://api.themoviedb.org"

object NetworkInteractor {

    private lateinit var apiKey: String

    fun setup(apiKey: String) {
        this.apiKey = apiKey
    }

    val moshi by lazy {
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
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}

internal inline fun Retrofit.Builder.callFactory(crossinline body: (Request) -> Call) =
    callFactory(object : Call.Factory {
        override fun newCall(request: Request): Call = body(request)
    })

internal fun Retrofit.Builder.delegatingCallFactory(delegate: Lazy<OkHttpClient>): Retrofit.Builder =
    callFactory { delegate.value.newCall(it) }