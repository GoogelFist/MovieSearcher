package com.github.googelfist.moviesearcher.data.datasourse.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_item")
data class MovieItemDAO(
    @PrimaryKey
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
