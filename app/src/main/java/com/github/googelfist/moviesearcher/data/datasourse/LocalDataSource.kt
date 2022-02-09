package com.github.googelfist.moviesearcher.data.datasourse

import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieDetailDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePreviewDAO

interface LocalDataSource {
    suspend fun loadTop250BestFilms(page: Int): MoviePreviewDAO

    suspend fun loadMovieDetail(id: Int): MovieDetailDAO
}