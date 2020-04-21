package com.harryio.fizz.network

import okhttp3.Interceptor
import okhttp3.Response

internal object UrlSchemeInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val originalUrl = request.url
        if (request.header("Use-Https") != null) {
            val newUrl = originalUrl.newBuilder()
                .scheme("https")
                .build()
            request = request.newBuilder()
                .url(newUrl)
                .build()
        }

        return chain.proceed(request)
    }
}