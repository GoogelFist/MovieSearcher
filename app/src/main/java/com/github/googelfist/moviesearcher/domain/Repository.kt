package com.github.googelfist.moviesearcher.domain

import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList

interface Repository {
    suspend fun loadPageTop250BestFilms(): List<MovieList>

    suspend fun loadMovieDetail(id: Int): MovieItem
}