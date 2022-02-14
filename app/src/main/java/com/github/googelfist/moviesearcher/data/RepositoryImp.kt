package com.github.googelfist.moviesearcher.data

import com.github.googelfist.moviesearcher.data.datasourse.LocalDataSource
import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.data.datasourse.local.model.PageCountDAO
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

        top250PageCount = localLoadPageCount()
        if (top250PageCount == PAGE_COUNT_ZERO) {
            top250PageCount = getUpdatedPageCount()
        }

        when {
            pageNumber == PAGE_COUNT_ONE -> {
                var movies = localLoadMovieList(PAGE_COUNT_ONE)
                movies?.let {
                    previewMovies = it as MutableList<MovieList>
                    increasePageNumber()
                    return previewMovies.toList()
                }
                updateMovieList(PAGE_COUNT_ONE)

                movies = localLoadMovieList(PAGE_COUNT_ONE)
                previewMovies = movies as MutableList<MovieList>
                increasePageNumber()
            }
            pageNumber < top250PageCount -> {
                var movies = localLoadMovieList(pageNumber)
                movies?.let {
                    previewMovies.addAll(it)
                    increasePageNumber()
                    return previewMovies.toList()
                }
                updateMovieList(pageNumber)

                movies = localLoadMovieList(pageNumber) ?: throw RuntimeException("Movies is not present")
                previewMovies.addAll(movies)
                increasePageNumber()
            }
        }
        return previewMovies.toList()
    }

    override suspend fun loadMovieItem(id: Int): MovieItem {
        var movieItem = localLoadMovieItem(id)
        movieItem?.let { return it }

        updateMovieItem(id)

        movieItem = localLoadMovieItem(id)
        return movieItem ?: throw RuntimeException("Movie item is not present")
    }

    override suspend fun updateMovieList(page: Int) {
        val remoteMovieList = remoteLoadMovieList(page)
        localSaveMovieList(remoteMovieList)
    }

    override suspend fun updateMovieItem(id: Int) {
        val remoteMovieItem = remoteLoadMovieItem(id)
        localSaveMovieItem(remoteMovieItem)
    }

    private suspend fun localLoadMovieList(page: Int): List<MovieList>? {
        val result = localDataSource.loadMoviePageList(page)
        result?.let { return mapper.mapMoviePageListDAOtoMovieList(it) }
        return null
    }

    private suspend fun remoteLoadMovieList(page: Int): List<MovieList> {
        try {
            val result = remoteDataSource.loadMovieList(page)

            return mapper.mapMovieListDTOtoMovieList(result)
        } catch (error: Throwable) {
            throw RemoteLoadMovieListError("Unable to load top 250 best films", error)
        }
    }

    private suspend fun localSaveMovieList(movieList: List<MovieList>) {
        val moviePageListDAO = mapper.mapMovieListToMoviePageListDAO(pageNumber, movieList)
        localDataSource.insertMoviePageList(moviePageListDAO)
    }

    private suspend fun localLoadMovieItem(id: Int): MovieItem? {
        val localResult = localDataSource.loadMovieItem(id)
        localResult?.let { return mapper.mapMovieItemDAOToMovieItem(it) }
        return null
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

    private suspend fun getUpdatedPageCount(): Int {
        val pageCount = remoteDataSource.loadMovieList(PAGE_COUNT_ONE).pagesCount
        localSavePageCountDAO(pageCount)
        return pageCount
    }

    private suspend fun localLoadPageCount(): Int {
        val pageCountDAO = localDataSource.loadPageCount()
        pageCountDAO?.let { return it.pageCount }
        return PAGE_COUNT_ZERO
    }

    private suspend fun localSavePageCountDAO(pageCount: Int) {
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