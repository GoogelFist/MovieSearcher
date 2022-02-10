package com.github.googelfist.moviesearcher.data.datasourse

import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieDetailDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePreviewListDAO

interface LocalDataSource {
    suspend fun loadTop250BestFilms(page: Int): MoviePreviewListDAO

    suspend fun insertMoviePreviewList(moviesPreviewDAO: MoviePreviewListDAO)

    suspend fun loadMovieDetail(id: Int): MovieDetailDAO

    suspend fun insertMovieDetail(movieDetail: MovieDetailDAO)
}