package com.harryio.fizz.movie_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.harryio.fizz.common_feature.BaseFragment
import com.harryio.fizz.movie_list.adapter.MovieListAdapter
import com.harryio.fizz.movie_list.databinding.FragmentMovieListBinding

class MovieListFragment : BaseFragment() {

    private val viewModel by viewModels<MovieListViewModel>()
    private lateinit var binding: FragmentMovieListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        binding.model = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieListAdapter = MovieListAdapter()
        binding.movielistRecyclerview.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = movieListAdapter
        }

        viewModel.movieList.observe(viewLifecycleOwner, Observer {
            movieListAdapter.submitList(it)
        })

    }
}