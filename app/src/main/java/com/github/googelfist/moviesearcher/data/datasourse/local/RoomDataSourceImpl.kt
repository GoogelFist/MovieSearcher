package com.github.googelfist.moviesearcher.data.datasourse.local

import com.github.googelfist.moviesearcher.data.datasourse.LocalDataSource
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieItemDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePageListDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.PageCountDAO
import javax.inject.Inject

class RoomDataSourceImpl @Inject constructor(private val movieDAO: MovieDAO) : LocalDataSource {

    override suspend fun loadMoviePageList(page: Int): MoviePageListDAO?{
        return movieDAO.loadMoviePageList(page)
    }

    override suspend fun insertMoviePageList(
        moviesPageDAO: MoviePageListDAO
    ) {
        movieDAO.insertMoviePageList(moviesPageDAO)
    }

    override suspend fun loadMovieItem(id: Int): MovieItemDAO {
        return movieDAO.loadMovieItem(id)
    }

    override suspend fun insertMovieItem(movieItem: MovieItemDAO) {
        movieDAO.insertMovieItem(movieItem)
    }

    override suspend fun loadPageCount(): PageCountDAO? {
        return movieDAO.loadPageCount()
    }

    override suspend fun insertPageCount(pageCount: PageCountDAO) {
        movieDAO.insertPageCount(pageCount)
    }
}