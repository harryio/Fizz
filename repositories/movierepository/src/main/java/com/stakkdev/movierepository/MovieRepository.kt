package com.stakkdev.movierepository

import com.harryio.fizz.common.FizzNetworkException
import com.harryio.fizz.common.Movie
import com.harryio.fizz.domain.PagedResponse
import com.harryio.fizz.domain.makeApiCall
import com.harryio.fizz.network.FizzNetworkInteractor
import io.github.sainiharry.basenetwork.NetworkInteractor
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