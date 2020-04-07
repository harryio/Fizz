package com.harryio.fizz.network

import com.squareup.moshi.Moshi
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_API_URL = "https://api.themoviedb.org/3/"

class NetworkInteractor {

    companion object {

        private var movieService: MovieService? = null

        private var moshi: Moshi? = null

        private var retrofit: Retrofit? = null

        private var okHttpClient: OkHttpClient? = null

        fun getMovieService(apiKey: String): MovieService {
            if (movieService == null) {
                movieService = getRetrofit(apiKey).create(MovieService::class.java)
            }

            return movieService!!
        }

        internal fun getMoshi(): Moshi {
            if (moshi == null) {
                moshi = Moshi.Builder().build()
            }

            return moshi!!
        }

        private fun getRetrofit(apiKey: String): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder().baseUrl(BASE_API_URL)
                    .client(getOkHttpClient(apiKey))
                    .addCallAdapterFactory(ApiResponseAdapter())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .addConverterFactory(MoshiConverterFactory.create(getMoshi()))
                    .build()
            }

            return retrofit!!
        }

        private fun getOkHttpClient(apiKey: String): OkHttpClient {
            if (okHttpClient == null) {
                okHttpClient =
                    OkHttpClient.Builder().addInterceptor(ApiKeyInterceptor(apiKey)).build()
            }

            return okHttpClient!!
        }
    }
}
