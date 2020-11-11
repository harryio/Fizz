package com.stakkdev.movierepository

import com.harryio.fizz.common.FizzNetworkException
import com.harryio.fizz.common.Movie
import com.harryio.fizz.domain.PagedResponse
import com.harryio.fizz.domain.makeApiCall
import kotlinx.coroutines.CoroutineDispatcher

interface MovieRepository {

    @Throws(FizzNetworkException::class)
    suspend fun getPopularMovies(): List<Movie>
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