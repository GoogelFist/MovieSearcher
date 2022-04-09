package com.github.googelfist.moviesearcher.domain

import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList

interface Repository {
    suspend fun updateMovieList()

    suspend fun loadMovieList(): List<MovieList>

    suspend fun updateMovieItem(id: Int)

    suspend fun loadMovieItem(id: Int): MovieItem?

    suspend fun loadPageCount(): Int

    suspend fun refreshLocalData(page: Int)
}