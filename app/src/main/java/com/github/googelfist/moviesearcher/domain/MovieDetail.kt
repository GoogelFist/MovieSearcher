package com.github.googelfist.moviesearcher.domain

import javax.sql.StatementEvent

data class MovieDetail(
    val kinopoiskId: Long,
    val nameRu: String,
    val nameEn: String,
    val posterUrl: String,
    val ratingKinopoisk: Float,
    val webUrl: String,
    val year: Int,
    val description: String,
    val country: String,
    val genre: String
)
