package com.github.googelfist.moviesearcher.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import com.github.googelfist.moviesearcher.R
import com.github.googelfist.moviesearcher.domain.model.MovieList

class MoviesPreviewAdapter :
    ListAdapter<MovieList, MoviesPreviewViewHolder>(MoviePreviewDiffCallback()) {

    var onMovieItemClickListener: ((ImageView, Int) -> Unit)? = null
    var onScrolledToBottomListener: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesPreviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_list_cell, parent, false)
        return MoviesPreviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesPreviewViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie, position)
        holder.imagePreview.setOnClickListener {
            onMovieItemClickListener?.invoke(it as ImageView, movie.kinopoiskId)
        }

        if ((position >= itemCount - ONE_VALUE))
            onScrolledToBottomListener?.invoke()
    }

    companion object {
        private const val ONE_VALUE = 1
    }
}