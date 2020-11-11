package com.harryio.fizz.movie_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.harryio.fizz.common.Movie
import com.harryio.fizz.movie_list.R

class MovieListAdapter(val movieList: List<Movie>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflator = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflator, R.layout.movie_list_item,parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    inner class MovieViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        internal fun bind(movie: Movie) {

        }
    }
}