package com.stakkdev.movierepository

import com.harryio.fizz.domain.PagedResponse
import retrofit2.http.GET
import retrofit2.http.Path

internal interface MovieService {

    @GET("/movie/{category}")
    suspend fun getMoviesForCategory(@Path("category") category: String): PagedResponse<MovieResponse>
}