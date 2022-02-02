package com.github.googelfist.moviesearcher.presentation.recycler

import androidx.recyclerview.widget.DiffUtil
import com.github.googelfist.moviesearcher.domain.model.MoviePreview

class MoviePreviewDiffCallback : DiffUtil.ItemCallback<MoviePreview>() {
    override fun areItemsTheSame(oldItem: MoviePreview, newItem: MoviePreview): Boolean {
        return oldItem.kinopoiskId == newItem.kinopoiskId
    }

    override fun areContentsTheSame(oldItem: MoviePreview, newItem: MoviePreview): Boolean {
        return oldItem == newItem
    }
}