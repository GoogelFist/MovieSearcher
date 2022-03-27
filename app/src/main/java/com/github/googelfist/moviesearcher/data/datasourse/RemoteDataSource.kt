package com.github.googelfist.moviesearcher.data.datasourse

import com.github.googelfist.moviesearcher.data.datasourse.network.model.item.MovieItemDTO
import com.github.googelfist.moviesearcher.data.datasourse.network.model.list.MovieListDTO
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList

interface RemoteDataSource {
    suspend fun loadMovieList(page: Int): List<MovieList>

    suspend fun loadMovieItem(id: Int): MovieItem

    suspend fun loadPageCount(page: Int): Int
}