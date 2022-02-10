package com.github.googelfist.moviesearcher.domain.model

data class MovieList(
    val kinopoiskId: Int,
    val nameRu: String,
    val nameEn: String?,
    val posterUrl: String
)
