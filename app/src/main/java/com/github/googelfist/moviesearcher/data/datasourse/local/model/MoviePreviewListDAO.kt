package com.github.googelfist.moviesearcher.data.datasourse.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_preview_list_dao")
data class MoviePreviewListDAO(
    @PrimaryKey val page: Int,
    val moviesPreview: List<MoviePreviewDAO>
)
