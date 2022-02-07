package com.github.googelfist.moviesearcher.domain.model

data class MoviePreview(
    val kinopoiskId: Int,
    val nameRu: String,
    val nameEn: String?,
    val posterUrl: String
)
