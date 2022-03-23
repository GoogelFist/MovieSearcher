package com.github.googelfist.moviesearcher.data.datasourse.local

import com.github.googelfist.moviesearcher.data.datasourse.LocalDataSource
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieItemDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePageListDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.PageCountDAO
import com.github.googelfist.moviesearcher.data.mapper.MovieMapper
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList
import javax.inject.Inject

class RoomDataSourceImpl @Inject constructor(private val movieDAO: MovieDAO, private val mapper: MovieMapper) : LocalDataSource {

    override suspend fun loadMoviePageList(page: Int): List<MovieList>? {
        val result = movieDAO.loadMoviePageList(page)
        result?.let { return mapper.mapMoviePageListDAOtoMovieList(it) }
        return null
    }

    override suspend fun insertMoviePageList(pageNumber: Int, movieList: List<MovieList>) {
        val moviePageListDAO = mapper.mapMovieListToMoviePageListDAO(pageNumber, movieList)
        movieDAO.insertMoviePageList(moviePageListDAO)
    }

    override suspend fun loadMovieItem(id: Int): MovieItem? {
        val result = movieDAO.loadMovieItem(id)
        result?.let { return mapper.mapMovieItemDAOToMovieItem(it) }
        return null
    }

    override suspend fun insertMovieItem(movieItem: MovieItem) {
        val movieItemDAO = mapper.mapMovieItemToMovieItemDAO(movieItem)
        movieDAO.insertMovieItem(movieItemDAO)
    }

    override suspend fun loadPageCount(): Int? {
        val pageCountDAO = movieDAO.loadPageCount()
        return pageCountDAO?.pageCount
    }

    override suspend fun insertPageCount(pageCount: Int) {
        movieDAO.insertPageCount(PageCountDAO(pageCount))
    }
}