package com.harryio.fizz.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit

private const val BASE_API_URL = "https://api.themoviedb.org/"

class NetworkInteractor {

    companion object {

        private var movieService: MovieService? = null

        private var retrofit: Retrofit? = null

        private var okHttpClient: OkHttpClient? = null

        fun getMovieService(): MovieService {
            if (movieService == null) {
                movieService = getRetrofit().create(MovieService::class.java)
            }

            return movieService!!
        }

        private fun getRetrofit(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder().baseUrl(BASE_API_URL)
                    .client(getOkHttpClient())
                    .build()
            }

            return retrofit!!
        }

        private fun getOkHttpClient(): OkHttpClient {
            if (okHttpClient == null) {
                okHttpClient = OkHttpClient.Builder().build()
            }

            return okHttpClient!!
        }
    }
}
