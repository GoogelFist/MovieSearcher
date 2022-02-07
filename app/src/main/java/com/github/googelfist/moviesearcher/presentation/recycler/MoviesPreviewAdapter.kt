package com.github.googelfist.moviesearcher.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import com.github.googelfist.moviesearcher.R
import com.github.googelfist.moviesearcher.domain.model.MoviePreview

class MoviesPreviewAdapter :
    ListAdapter<MoviePreview, MoviesPreviewViewHolder>(MoviePreviewDiffCallback()) {

    lateinit var onMoviePreviewClickListener: ((ImageView) -> Unit)
    lateinit var getMoviePreviewId: ((Int) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesPreviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_preview_cell, parent, false)
        return MoviesPreviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesPreviewViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie, position)
        holder.imagePreview.setOnClickListener {
            onMoviePreviewClickListener.invoke(it as ImageView)
            getMoviePreviewId.invoke(movie.kinopoiskId)
        }
    }
}