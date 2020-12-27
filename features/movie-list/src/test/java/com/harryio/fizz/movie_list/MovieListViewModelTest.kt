package com.harryio.fizz.movie_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.stakkdev.fizz.common.Movie
import com.stakkdev.fizz.movierepository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieListViewModelTest {

    @Mock
    private lateinit var movieRepository: MovieRepository

    @Mock
    private lateinit var movieListObserver: Observer<List<Movie>>

    private lateinit var movieListViewModel: MovieListViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        movieListViewModel = MovieListViewModel(movieRepository, TestCoroutineDispatcher())
        movieListViewModel.movieList.observeForever(movieListObserver)
    }

    @After
    fun tearDown() {
        movieListViewModel.movieList.removeObserver(movieListObserver)
    }

    private fun getMockMovie() = Movie(
        1,
        "Movie Title",
        "Movie Overview",
        null,
        null,
        false,
        null,
        "Movie Original Title",
        null,
        0f,
        0,
        false,
        0f
    )
}