package com.github.googelfist.moviesearcher.data

import com.github.googelfist.moviesearcher.data.datasourse.LocalDataSource
import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.domain.Repository
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {

    private var pageNumber: Int = PAGE_COUNT_ONE
    private var top250PageCount: Int = PAGE_COUNT_ZERO

    private var previewMovies = mutableListOf<MovieList>()

    override suspend fun loadMovieList(): List<MovieList> {

        top250PageCount = loadPageCount()

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
                    ?: throw RuntimeException("Movies is not present")
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

                movies = localLoadMovieList(pageNumber)
                    ?: throw RuntimeException("Movies is not present")
                previewMovies.addAll(movies)
                increasePageNumber()
            }
        }
        return previewMovies.toList()
    }

    override suspend fun loadMovieItem(id: Int): MovieItem? {
        val movieItem = localLoadMovieItem(id)
        movieItem?.let { return it }

        val remoteMovieItem = remoteLoadMovieItem(id)
        remoteMovieItem?.let {
            localSaveMovieItem(remoteMovieItem)
            return remoteMovieItem
        }
        return null
    }

    override suspend fun loadPageCount(): Int {
        val pageCount = localLoadPageCount()
        if (pageCount == PAGE_COUNT_ZERO) {
            return getUpdatedPageCount()
        }
        return pageCount
    }

    override suspend fun refreshLocalData(page: Int) {
        val remoteMovieList = remoteLoadMovieList(page)
        localSaveMovieList(page, remoteMovieList)
        withContext(Dispatchers.IO) {
            for (item in remoteMovieList) {
                val itemId = item.kinopoiskId
                val remoteLoadMovieItem = remoteLoadMovieItem(itemId)
                remoteLoadMovieItem?.let {
                    localSaveMovieItem(remoteLoadMovieItem)
                }
                delay(300)
            }
        }
    }

    private suspend fun localLoadMovieList(page: Int): List<MovieList>? {
        return localDataSource.loadMoviePageList(page)
    }

    private suspend fun updateMovieList(page: Int) {
        val remoteMovieList = remoteLoadMovieList(page)
        localSaveMovieList(page, remoteMovieList)
    }

    private suspend fun remoteLoadMovieList(page: Int): List<MovieList> {
        try {
            return remoteDataSource.loadMovieList(page)
        } catch (error: Throwable) {
            throw RemoteLoadMovieListError("Unable to load top 250 best films", error)
        }
    }

    private suspend fun localSaveMovieList(pageNumber: Int, movieList: List<MovieList>) {
        localDataSource.insertMoviePageList(pageNumber, movieList)
    }

    private suspend fun localLoadMovieItem(id: Int): MovieItem? {
        return localDataSource.loadMovieItem(id)
    }

    private suspend fun remoteLoadMovieItem(id: Int): MovieItem? {
        return try {
            remoteDataSource.loadMovieItem(id)
        } catch (error: Throwable) {
            null
        }
    }

    private suspend fun localSaveMovieItem(movieItem: MovieItem) {
        localDataSource.insertMovieItem(movieItem)
    }

    private suspend fun getUpdatedPageCount(): Int {
        try {
            val pageCount = remoteDataSource.loadPageCount(PAGE_COUNT_ONE)
            localSavePageCountDAO(pageCount)
            return pageCount
        } catch (error: Throwable) {
            throw RemoteLoadPageCountError("Unable to load page count", error)
        }
    }

    private suspend fun localLoadPageCount(): Int {
        val pageCount = localDataSource.loadPageCount()
        return pageCount ?: PAGE_COUNT_ZERO
    }

    private suspend fun localSavePageCountDAO(pageCount: Int) {
        localDataSource.insertPageCount(pageCount)
    }

    private fun increasePageNumber() {
        pageNumber++
    }

    companion object {
        private const val PAGE_COUNT_ZERO = 0
        private const val PAGE_COUNT_ONE = 1
    }
}

open class RemoteLoadError(message: String, cause: Throwable) : Throwable(message, cause)
class RemoteLoadMovieListError(message: String, cause: Throwable) : RemoteLoadError(message, cause)
class RemoteLoadPageCountError(message: String, cause: Throwable) : RemoteLoadError(message, cause)