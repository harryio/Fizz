package com.harryio.fizz.movie_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.harryio.fizz.common.Movie
import com.harryio.fizz.common_feature.BaseViewModel
import com.stakkdev.movierepository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MovieListViewModel constructor(
    private val movieRepository: MovieRepository,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
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