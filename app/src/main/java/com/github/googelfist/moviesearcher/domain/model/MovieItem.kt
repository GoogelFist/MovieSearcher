package com.github.googelfist.moviesearcher.domain.model

data class MovieItem(
    val kinopoiskId: Int,
    val nameRu: String,
    val nameEn: String,
    val nameOriginal: String,
    val posterUrl: String,
    val ratingKinopoisk: String,
    val year: String,
    val description: String,
    val country: String,
    val genre: String?,
    val webUrl: String
)
