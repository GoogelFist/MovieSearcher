package com.github.googelfist.moviesearcher.data.datasourse

import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieItemDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePageListDAO

interface LocalDataSource {
    suspend fun loadTop250BestFilms(page: Int): MoviePageListDAO

    suspend fun insertMoviePreviewList(moviesPageDAO: MoviePageListDAO)

    suspend fun loadMovieDetail(id: Int): MovieItemDAO

    suspend fun insertMovieDetail(movieItem: MovieItemDAO)
}