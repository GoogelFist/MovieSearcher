package com.github.googelfist.moviesearcher.data.datasourse.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_page_list_dao")
data class MoviePageListDAO(
    @PrimaryKey
    val page: Int,
    val moviesList: List<MovieListDAO>
)
