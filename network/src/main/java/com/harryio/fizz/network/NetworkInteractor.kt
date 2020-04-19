package com.harryio.fizz.network

import com.squareup.moshi.Moshi
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_API_URL = "https://api.themoviedb.org"

class NetworkInteractor(private val apiKey: String) {

    companion object {

        internal val moshi by lazy { Moshi.Builder().build() }

        private var networkInteractor: NetworkInteractor? = null

        fun getMovieService(apiKey: String): MovieService {
            if (networkInteractor == null) {
                networkInteractor = NetworkInteractor(apiKey)
            }

            return networkInteractor!!.movieService
        }
    }

    private val movieService by lazy { retrofit.create(MovieService::class.java) }

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_API_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(ApiResponseAdapter())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor(ApiKeyInterceptor(apiKey))
            .addInterceptor(HttpLoggingInterceptor()).build()
    }
}
