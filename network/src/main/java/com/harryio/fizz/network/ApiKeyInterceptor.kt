package com.harryio.fizz.network

import okhttp3.Interceptor
import okhttp3.Response

private const val API_KEY_QUERY_PARAM = "api_key"

internal class ApiKeyInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var originalRequest = chain.request()
        val originalUrl = originalRequest.url
        if (originalRequest.header("Require-Api-Key") == null) {
            val newUrl = originalUrl.newBuilder()
                .addQueryParameter(API_KEY_QUERY_PARAM, apiKey)
                .build()

            originalRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()
        }

        return chain.proceed(originalRequest)
    }
}