package com.harryio.fizz.movie_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.stakkdev.fizz.common.Movie
import com.stakkdev.common_feature.BaseViewModel
import com.stakkdev.fizz.movierepository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MovieListViewModel constructor(
    private val movieRepository: MovieRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val _movieList = MutableLiveData<List<Movie>>()
    internal val movieList: LiveData<List<Movie>>
        get() = _movieList

    init {
        fetchMovieList()
    }

    private fun fetchMovieList() = viewModelScope.launch(coroutineDispatcher) {
        _movieList.value = movieRepository.getPopularMovies()
    }
}