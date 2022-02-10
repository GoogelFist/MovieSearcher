package com.github.googelfist.moviesearcher.data.datasourse.local

import com.github.googelfist.moviesearcher.data.datasourse.LocalDataSource
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieDetailDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePreviewDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePreviewListDAO
import javax.inject.Inject

class RoomDataSourceImpl @Inject constructor(private val movieDAO: MovieDAO) : LocalDataSource {

    override suspend fun loadTop250BestFilms(page: Int): MoviePreviewListDAO{
        return movieDAO.loadTop250BestFilms(page)
    }

    override suspend fun insertMoviePreviewList(
        moviesPreviewDAO: MoviePreviewListDAO
    ) {
        movieDAO.insertMoviePreviewList(moviesPreviewDAO)
    }

    override suspend fun loadMovieDetail(id: Int): MovieDetailDAO {
        return movieDAO.loadMovieDetail(id)
    }

    override suspend fun insertMovieDetail(movieDetail: MovieDetailDAO) {
        movieDAO.insertMovieDetail(movieDetail)
    }
}