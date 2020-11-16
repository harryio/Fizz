package com.harryio.fizz.movie_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harryio.fizz.common.Movie
import com.harryio.fizz.movie_list.R
import com.harryio.fizz.movie_list.databinding.MovieListItemBinding

private val listDiffer = object : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
}

class MovieListAdapter : ListAdapter<Movie, MovieViewHolder>(listDiffer) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflator = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<MovieListItemBinding>(
                layoutInflator,
                R.layout.movie_list_item,
                parent,
                false
            )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MovieViewHolder(private val binding: MovieListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    internal fun bind(movie: Movie) {
        binding.movie = movie
    }
}