package com.github.googelfist.moviesearcher.data.datasourse

import com.github.googelfist.moviesearcher.data.datasourse.network.model.detail.MovieItemDTO
import com.github.googelfist.moviesearcher.data.datasourse.network.model.preview.MovieListDTO

interface RemoteDataSource {
    suspend fun loadTop250BestFilms(page: Int): MovieListDTO

    suspend fun loadMovieDetail(id: Int): MovieItemDTO
}