package com.github.googelfist.moviesearcher.data.datasourse

import com.github.googelfist.moviesearcher.data.datasourse.network.model.item.MovieItemDTO
import com.github.googelfist.moviesearcher.data.datasourse.network.model.list.MovieListDTO

interface RemoteDataSource {
    suspend fun loadTop250BestFilms(page: Int): MovieListDTO

    suspend fun loadMovieDetail(id: Int): MovieItemDTO
}