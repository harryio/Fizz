package com.stakkdev.fizz.movierepository

import com.stakkdev.fizz.domain.PagedResponse
import retrofit2.http.GET
import retrofit2.http.Path

internal interface MovieService {

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(): PagedResponse<MovieResponse>
}