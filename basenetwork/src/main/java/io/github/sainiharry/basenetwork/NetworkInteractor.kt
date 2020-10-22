package io.github.sainiharry.basenetwork

import com.squareup.moshi.Moshi
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import okio.BufferedSource
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

open class NetworkInteractor protected constructor(private val apiUrl: String) {

    private val moshi: Moshi by lazy {
        val moshiBuilder = Moshi.Builder()
        onMoshiBuilderConstructed(moshiBuilder)
        moshiBuilder.build()
    }

    private val okHttpClient = lazy {
        val okHttpClientBuilder = OkHttpClient.Builder()
        onOkHttpClientBuilderConstructed(okHttpClientBuilder)
        okHttpClientBuilder
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
            .build()
    }

    val retrofit: Retrofit by lazy {
        val retrofitBuilder = Retrofit.Builder()
        onRetrofitBuilderConstructed(retrofitBuilder)
        retrofitBuilder
            .baseUrl(apiUrl)
            .delegatingCallFactory(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    fun <T> parseJson(source: BufferedSource, clazz: Class<T>): T? =
        moshi.adapter(clazz).fromJson(source)

    protected open fun onMoshiBuilderConstructed(moshiBuilder: Moshi.Builder) {}

    protected open fun onOkHttpClientBuilderConstructed(okHttpClientBuilder: OkHttpClient.Builder) {}

    protected open fun onRetrofitBuilderConstructed(retrofitBuilder: Retrofit.Builder) {}
}

internal inline fun Retrofit.Builder.callFactory(crossinline body: (Request) -> Call) =
    callFactory(object : Call.Factory {
        override fun newCall(request: Request): Call = body(request)
    })

internal fun Retrofit.Builder.delegatingCallFactory(delegate: Lazy<OkHttpClient>): Retrofit.Builder =
    callFactory { delegate.value.newCall(it) }