package com.github.googelfist.moviesearcher.domain

import androidx.lifecycle.LiveData
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList

interface Repository {
    suspend fun updateMovieList()

    fun loadMovieList(): LiveData<List<MovieList>>

    suspend fun updateMovieItem(id: Int)

    fun loadMovieItem(id: Int): LiveData<MovieItem>

    suspend fun loadPageCount(): Int

    suspend fun refreshLocalData(page: Int)
}