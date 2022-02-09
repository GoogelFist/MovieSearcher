package com.github.googelfist.moviesearcher.data.datasourse.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoviePreviewDAO(
    @PrimaryKey val kinopoiskId: Int,
    val nameRu: String,
    val nameEn: String?,
    val posterUrl: String
)
