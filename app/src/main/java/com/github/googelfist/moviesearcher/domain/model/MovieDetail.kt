package com.github.googelfist.moviesearcher.domain.model

import javax.sql.StatementEvent

data class MovieDetail(
    val kinopoiskId: Long,
    val nameRu: String?,
    val nameEn: String?,
    val posterUrl: String?,
    val ratingKinopoisk: String?,
    val webUrl: String?,
    val year: String?,
    val description: String?,
    val country: String?,
    val genre: String?
)
