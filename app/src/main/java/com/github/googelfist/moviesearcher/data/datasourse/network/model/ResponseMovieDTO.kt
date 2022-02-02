package com.github.googelfist.moviesearcher.data.datasourse.network.model

data class ResponseMovieDTO(
    val films: List<Film>,
    val pagesCount: Int
)
