package com.stakkdev.fizz.movierepository

import com.stakkdev.fizz.network.FizzNetworkInteractor
import com.stakkdev.fizz.common.FizzNetworkException
import com.stakkdev.fizz.common.Movie
import com.stakkdev.fizz.domain.makeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface MovieRepository {

    @Throws(FizzNetworkException::class)
    suspend fun getPopularMovies(): List<Movie>

    companion object {

        private var movieRepository: MovieRepository? = null

        fun getInstance(): MovieRepository {
            return movieRepository ?: MovieRepositoryImpl(
                FizzNetworkInteractor.retrofit.create(
                    MovieService::class.java
                ), Dispatchers.IO
            ).also {
                movieRepository = it
            }
        }
    }
}

internal class MovieRepositoryImpl(
    private val movieService: MovieService,
    private val coroutineDispatcher: CoroutineDispatcher
) : MovieRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        val moviesResults = makeApiCall(coroutineDispatcher) {
            movieService.getPopularMovies().results
        }

        return moviesResults.filter { it.isValid() }
            .map { it.toMovie() }
    }
}