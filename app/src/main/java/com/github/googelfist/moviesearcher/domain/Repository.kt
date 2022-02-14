package com.github.googelfist.moviesearcher.domain

import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList

interface Repository {
    suspend fun loadMovieList(): List<MovieList>

    suspend fun loadMovieItem(id: Int): MovieItem

    suspend fun updateMovieList(page: Int)

    suspend fun updateMovieItem(id: Int)
}