package com.github.googelfist.moviesearcher.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import com.github.googelfist.moviesearcher.R
import com.github.googelfist.moviesearcher.domain.model.MovieList

class MoviesPreviewAdapter :
    ListAdapter<MovieList, MoviesPreviewViewHolder>(MoviePreviewDiffCallback()) {

    lateinit var onMoviePreviewClickListener: ((ImageView, Int) -> Unit)
    lateinit var onScrolledToBottom: (() -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesPreviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_list_cell, parent, false)
        return MoviesPreviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesPreviewViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie, position)
        holder.imagePreview.setOnClickListener {
            onMoviePreviewClickListener.invoke(it as ImageView, movie.kinopoiskId)
        }

        if ((position >= itemCount - ONE_VALUE))
            onScrolledToBottom.invoke()
    }

    companion object {
        private const val ONE_VALUE = 1
    }
}