package com.github.googelfist.moviesearcher.presentation.recycler

import androidx.recyclerview.widget.DiffUtil
import com.github.googelfist.moviesearcher.domain.model.MovieList

class MoviePreviewDiffCallback : DiffUtil.ItemCallback<MovieList>() {
    override fun areItemsTheSame(oldItem: MovieList, newItem: MovieList): Boolean {
        return oldItem.kinopoiskId == newItem.kinopoiskId
    }

    override fun areContentsTheSame(oldItem: MovieList, newItem: MovieList): Boolean {
        return oldItem == newItem
    }
}