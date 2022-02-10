package com.github.googelfist.moviesearcher.data

import com.github.googelfist.moviesearcher.data.datasourse.LocalDataSource
import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.data.datasourse.local.model.PageCountDAO
import com.github.googelfist.moviesearcher.data.datasourse.network.model.list.MovieListDTO
import com.github.googelfist.moviesearcher.data.mapper.MovieMapper
import com.github.googelfist.moviesearcher.domain.Repository
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val mapper: MovieMapper
) : Repository {

    private var pageNumber: Int = PAGE_COUNT_ONE
    private var top250PageCount: Int = PAGE_COUNT_ZERO

    private var previewMovies = mutableListOf<MovieList>()

    override suspend fun loadMovieList(): List<MovieList> {

        top250PageCount = updatePageCount()

        when {
            pageNumber == PAGE_COUNT_ONE -> {
                val movies = updateMovieList(PAGE_COUNT_ONE)
                previewMovies = movies as MutableList<MovieList>
                increasePageNumber()
            }
            pageNumber < top250PageCount -> {
                val movies = updateMovieList(pageNumber)
                previewMovies.addAll(movies)
                increasePageNumber()
            }
        }
        return previewMovies.toList()
    }

    override suspend fun loadMovieItem(id: Int): MovieItem {
        return updateMovieItem(id)
    }

    private suspend fun updateMovieList(page: Int): List<MovieList> {
        val result = localDataSource.loadMoviePageList(page)
        result?.let {
            return mapper.mapMoviePageListDAOtoMovieList(it)
        }
        val remoteMovieList = remoteLoadMovieList(page)
        localSaveMovieList(remoteMovieList)
        return remoteMovieList
    }

    private suspend fun remoteLoadMovieList(page: Int): List<MovieList> {
        try {
            val result = remoteDataSource.loadTop250BestFilms(page)

            localSavePageCountDAO(result)

            return mapper.mapMovieListDTOtoMovieList(result)
        } catch (error: Throwable) {
            throw RemoteLoadMovieListError("Unable to load top 250 best films", error)
        }
    }

    private suspend fun localSaveMovieList(movieList: List<MovieList>) {
        val moviePageListDAO = mapper.mapMovieListToMoviePageListDAO(pageNumber, movieList)
        localDataSource.insertMoviePageList(moviePageListDAO)
    }

    private suspend fun updateMovieItem(id: Int): MovieItem {
        val localResult = localDataSource.loadMovieItem(id)
        localResult?.let {
            return mapper.mapMovieItemDAOToMovieItem(it)
        }
        val remoteMovieItem = remoteLoadMovieItem(id)
        localSaveMovieItem(remoteMovieItem)
        return remoteMovieItem
    }

    private suspend fun remoteLoadMovieItem(id: Int): MovieItem {
        try {
            val result = remoteDataSource.loadMovieItem(id)

            return mapper.mapMovieItemDTOtoMovieItem(result)
        } catch (error: Throwable) {
            throw RemoteLoadMovieItemError("Unable to load movie detail", error)
        }
    }

    private suspend fun localSaveMovieItem(movieItem: MovieItem) {
        val movieItemDAO = mapper.mapMovieItemToMovieItemDAO(movieItem)
        localDataSource.insertMovieItem(movieItemDAO)
    }

    private suspend fun updatePageCount(): Int {
        val pageCountDAO = localDataSource.loadPageCount()
        pageCountDAO?.let {
            return pageCountDAO.pageCount
        }
        return PAGE_COUNT_ZERO
    }

    private suspend fun localSavePageCountDAO(result: MovieListDTO) {
        val pageCount = result.pagesCount
        localDataSource.insertPageCount(PageCountDAO(pageCount))
    }

    private fun increasePageNumber() {
        pageNumber++
    }

    companion object {
        private const val PAGE_COUNT_ZERO = 0
        private const val PAGE_COUNT_ONE = 1
    }
}

class RemoteLoadMovieListError(message: String, cause: Throwable) : Throwable(message, cause)
class RemoteLoadMovieItemError(message: String, cause: Throwable) : Throwable(message, cause)