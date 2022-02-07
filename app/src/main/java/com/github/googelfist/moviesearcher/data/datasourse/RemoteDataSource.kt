package com.github.googelfist.moviesearcher.data.datasourse

import com.github.googelfist.moviesearcher.data.datasourse.network.model.detail.MovieDetailDTO
import com.github.googelfist.moviesearcher.data.datasourse.network.model.preview.MoviePreviewDTO

interface RemoteDataSource {
    suspend fun loadTop250BestFilms(page: Int): MoviePreviewDTO

    suspend fun loadMovieDetail(id: Int): MovieDetailDTO
}