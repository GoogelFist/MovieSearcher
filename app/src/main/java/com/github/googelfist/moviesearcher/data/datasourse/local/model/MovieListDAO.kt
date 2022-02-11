package com.github.googelfist.moviesearcher.data.datasourse.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_list_dao")
data class MovieListDAO(
    @PrimaryKey
    val kinopoiskId: Int,
    val nameRu: String,
    val nameEn: String?,
    val posterUrl: String
)
