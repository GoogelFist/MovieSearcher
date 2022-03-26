package com.github.googelfist.moviesearcher.domain

import androidx.lifecycle.LiveData
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList

interface Repository {
    suspend fun fetchMovieList()

    fun loadMovieList(): LiveData<List<MovieList>>

    suspend fun loadMovieItem(id: Int): MovieItem?

    suspend fun loadPageCount(): Int

    suspend fun refreshLocalData(page: Int)
}