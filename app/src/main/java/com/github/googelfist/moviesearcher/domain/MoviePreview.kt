package com.github.googelfist.moviesearcher.domain

data class MoviePreview(
    val kinopoiskId: Long,
    val nameRu: String,
    val nameEn: String,
    val posterUrlPreview: String,
    val ratingKinopoisk: Float,
    val year: Int,
    val shortDescription: String,
    val country: String,
    val genre: String
)
