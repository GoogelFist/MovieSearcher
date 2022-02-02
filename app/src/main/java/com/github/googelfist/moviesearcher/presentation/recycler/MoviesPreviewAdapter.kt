package com.github.googelfist.moviesearcher.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.googelfist.moviesearcher.R
import com.github.googelfist.moviesearcher.domain.model.MoviePreview
import com.squareup.picasso.Picasso

class MoviesPreviewAdapter : ListAdapter<MoviePreview, MoviesPreviewViewHolder>(MoviePreviewDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesPreviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_preview_cell, parent, false)
        return MoviesPreviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesPreviewViewHolder, position: Int) {
        val movie = getItem(position)
        Picasso.get().load(movie.posterUrlPreview).into(holder.imagePreview)
        holder.nameEn.text = movie.nameEn
    }
}