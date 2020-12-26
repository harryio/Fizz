package com.stakkdev.movierepository

import com.harryio.fizz.domain.PagedResponse
import retrofit2.http.GET
import retrofit2.http.Path

internal interface MovieService {

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(): PagedResponse<MovieResponse>
}