package com.github.googelfist.moviesearcher.domain.model

data class MoviePreviewContainer(
    val errorMessage: String,
    val previewMovies: List<MoviePreview>
)
