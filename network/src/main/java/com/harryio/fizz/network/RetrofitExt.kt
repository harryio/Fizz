package com.harryio.fizz.network

import okhttp3.Call
import okhttp3.Call.Factory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit

internal inline fun Retrofit.Builder.callFactory(crossinline body: (Request) -> Call) =
    callFactory(object : Factory {
        override fun newCall(request: Request): Call = body(request)
    })

internal fun Retrofit.Builder.delegatingCallFactory(delegate: dagger.Lazy<OkHttpClient>): Retrofit.Builder =
    callFactory { delegate.get().newCall(it) }