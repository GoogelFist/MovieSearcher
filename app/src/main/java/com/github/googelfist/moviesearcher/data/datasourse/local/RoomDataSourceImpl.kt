package com.github.googelfist.moviesearcher.data.datasourse.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.github.googelfist.moviesearcher.data.datasourse.LocalDataSource
import com.github.googelfist.moviesearcher.data.datasourse.local.model.PageCountDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.PageNumberDAO
import com.github.googelfist.moviesearcher.data.mapper.MovieMapper
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList
import javax.inject.Inject

class RoomDataSourceImpl @Inject constructor(
    private val movieDAO: MovieDAO,
    private val mapper: MovieMapper
) : LocalDataSource {
    override fun loadAllMovieLists(): LiveData<List<MovieList>> {
        val result = movieDAO.loadAllMovieLists()
        return Transformations.map(result) {
            mapper.mapListMoviePageListDAOtoMovieList(it)
        }
    }

    override suspend fun insertMoviePageList(pageNumber: Int, movieList: List<MovieList>) {
        val moviePageListDAO = mapper.mapMovieListToMoviePageListDAO(pageNumber, movieList)
        movieDAO.insertMoviePageList(moviePageListDAO)
    }

    override fun loadMovieItem(id: Int): LiveData<MovieItem> {
        val result = movieDAO.loadMovieItem(id)
        return Transformations.map(result) {
            it?.let { mapper.mapMovieItemDAOToMovieItem(it) }
        }
    }

    override suspend fun insertMovieItem(movieItem: MovieItem) {
        val movieItemDAO = mapper.mapMovieItemToMovieItemDAO(movieItem)
        movieDAO.insertMovieItem(movieItemDAO)
    }

    override suspend fun loadPageCount(): Int {
        val pageCountDAO = movieDAO.loadPageCountById(PAGE_COUNT_ID)
        pageCountDAO?.let { return it.pageCount }
        return PAGE_COUNT_ZERO
    }

    override suspend fun insertPageCount(pageCount: Int) {
        movieDAO.insertPageCount(PageCountDAO(PAGE_COUNT_ID, pageCount))
    }

    override suspend fun loadPageNumber(): Int {
        val pageNumberDAO = movieDAO.loadPageNumberById(PAGE_NUMBER_ID)
        pageNumberDAO?.let { return it.pageNumber }
        return PAGE_COUNT_ONE
    }

    override suspend fun insertPageNumber(pageNumber: Int) {
        movieDAO.insertPageNumber(PageNumberDAO(PAGE_NUMBER_ID, pageNumber))
    }

    companion object {
        private const val PAGE_COUNT_ZERO = 0
        private const val PAGE_COUNT_ONE = 1

        private const val PAGE_COUNT_ID = 1
        private const val PAGE_NUMBER_ID = 1
    }
}
