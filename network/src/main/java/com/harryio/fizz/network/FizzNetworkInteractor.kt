package com.harryio.fizz.network

import io.github.sainiharry.basenetwork.NetworkInteractor
import okhttp3.OkHttpClient

private const val BASE_API_URL = "https://api.themoviedb.org"

object FizzNetworkInteractor : NetworkInteractor(BASE_API_URL) {

    private lateinit var apiKey: String

    fun setup(apiKey: String) {
        this.apiKey = apiKey
    }

    override fun onOkHttpClientBuilderConstructed(okHttpClientBuilder: OkHttpClient.Builder) {
        okHttpClientBuilder.addInterceptor(UrlSchemeInterceptor)
            .addInterceptor(ApiKeyInterceptor(apiKey))
    }
}