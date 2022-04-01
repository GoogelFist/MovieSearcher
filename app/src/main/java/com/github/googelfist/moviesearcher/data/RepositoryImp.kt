package com.github.googelfist.moviesearcher.data

import androidx.lifecycle.LiveData
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

    override suspend fun updateMovieList() {
        val totalPageCount = loadPageCount()
        var pageNumber = loadPageNumber()

        if (pageNumber <= totalPageCount) {
            val remoteMovieList = remoteLoadMovieList(pageNumber)
            localSaveMovieList(pageNumber, remoteMovieList)
            pageNumber++
            localSavePageNumberDAO(pageNumber)
        }
    }

    override fun loadMovieList(): LiveData<List<MovieList>> {
        return localDataSource.loadAllMovieLists()
    }

    override fun loadMovieItem(id: Int): LiveData<MovieItem> {
        return localDataSource.loadMovieItem(id)
    }

    override suspend fun updateMovieItem(id: Int) {
        val remoteMovieItem = remoteLoadMovieItem(id)
        localSaveMovieItem(remoteMovieItem)
    }

    override suspend fun loadPageCount(): Int {
        val pageCount = localDataSource.loadPageCount()
        if (pageCount == PAGE_COUNT_ZERO) {
            updatedPageCount()
            return localDataSource.loadPageCount()
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
                localSaveMovieItem(remoteLoadMovieItem)
                delay(REFRESH_DELAY)
            }
        }
    }

    private suspend fun loadPageNumber(): Int {
        return localDataSource.loadPageNumber()
    }

    private suspend fun remoteLoadMovieList(page: Int): List<MovieList> {
        try {
            return remoteDataSource.loadMovieList(page)
        } catch (error: Throwable) {
            throw RemoteLoadMovieListError(UNABLE_LOAD_MOVIE_LIST_ERROR_MESSAGE, error)
        }
    }

    private suspend fun localSaveMovieList(pageNumber: Int, movieList: List<MovieList>) {
        localDataSource.insertMoviePageList(pageNumber, movieList)
    }

    private suspend fun localSavePageNumberDAO(pageNumber: Int) {
        localDataSource.insertPageNumber(pageNumber)
    }

    private suspend fun remoteLoadMovieItem(id: Int): MovieItem {
        try {
            return remoteDataSource.loadMovieItem(id)
        } catch (error: Throwable) {
            throw RemoteLoadMovieItemError(UNABLE_LOAD_MOVIE_ITEM_ERROR_MESSAGE, error)
        }
    }

    private suspend fun localSaveMovieItem(movieItem: MovieItem) {
        localDataSource.insertMovieItem(movieItem)
    }

    private suspend fun updatedPageCount() {
        try {
            val pageCount = remoteDataSource.loadPageCount(PAGE_COUNT_ONE)
            localDataSource.insertPageCount(pageCount)
        } catch (error: Throwable) {
            throw RemoteLoadPageCountError(UNABLE_LOAD_PAGE_COUNT_ERROR_MESSAGE, error)
        }
    }

    companion object {
        private const val PAGE_COUNT_ZERO = 0
        private const val PAGE_COUNT_ONE = 1

        private const val REFRESH_DELAY = 300L

        private const val UNABLE_LOAD_MOVIE_LIST_ERROR_MESSAGE = "Unable to load movie list"
        private const val UNABLE_LOAD_MOVIE_ITEM_ERROR_MESSAGE = "Unable to load movie item"
        private const val UNABLE_LOAD_PAGE_COUNT_ERROR_MESSAGE = "Unable to load page count"
    }
}

open class RemoteLoadError(message: String, cause: Throwable) : Throwable(message, cause)
class RemoteLoadMovieListError(message: String, cause: Throwable) : RemoteLoadError(message, cause)
class RemoteLoadPageCountError(message: String, cause: Throwable) : RemoteLoadError(message, cause)
class RemoteLoadMovieItemError(message: String, cause: Throwable) : RemoteLoadError(message, cause)