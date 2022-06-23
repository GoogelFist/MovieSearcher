package com.github.googelfist.moviesearcher.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import com.github.googelfist.moviesearcher.R
import com.github.googelfist.moviesearcher.domain.model.MovieList

class MovieListAdapter :
    ListAdapter<MovieList, MovieListViewHolder>(MovieListDiffCallback()) {

    lateinit var onMovieItemClickListener: ((kinopoiskId :Int) -> Unit)
    lateinit var onScrolledToBottomListener: (() -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_list_cell, parent, false)

        val viewHolder = MovieListViewHolder(view)

        val imagePreview = viewHolder.imagePreview
        imagePreview.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != NO_POSITION) {
                val kinopoiskId = currentList[position].kinopoiskId
                onMovieItemClickListener.invoke(kinopoiskId)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie, position)

        if ((position >= itemCount - ONE_VALUE))
            onScrolledToBottomListener.invoke()
    }

    companion object {
        private const val ONE_VALUE = 1

        private const val NO_POSITION = -1
    }
}