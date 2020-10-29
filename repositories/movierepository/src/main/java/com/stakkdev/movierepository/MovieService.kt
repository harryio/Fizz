package com.stakkdev.movierepository

import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {

    @GET("/movie/{category}")
    suspend fun getMoviesByCategory(@Path("category") category : String): MoviesResponse
}