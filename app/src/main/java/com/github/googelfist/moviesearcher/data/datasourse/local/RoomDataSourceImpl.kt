package com.github.googelfist.moviesearcher.data.datasourse.local

import com.github.googelfist.moviesearcher.data.datasourse.LocalDataSource
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieItemDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePageListDAO
import javax.inject.Inject

class RoomDataSourceImpl @Inject constructor(private val movieDAO: MovieDAO) : LocalDataSource {

    override suspend fun loadTop250BestFilms(page: Int): MoviePageListDAO{
        return movieDAO.loadTop250BestFilms(page)
    }

    override suspend fun insertMoviePreviewList(
        moviesPageDAO: MoviePageListDAO
    ) {
        movieDAO.insertMoviePreviewList(moviesPageDAO)
    }

    override suspend fun loadMovieDetail(id: Int): MovieItemDAO {
        return movieDAO.loadMovieDetail(id)
    }

    override suspend fun insertMovieDetail(movieItem: MovieItemDAO) {
        movieDAO.insertMovieDetail(movieItem)
    }
}