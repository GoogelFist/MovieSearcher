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

    override suspend fun fetchMovieList() {
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
        val pageCount = localDataSource.loadPageCount()
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


    private suspend fun localSavePageCountDAO(pageCount: Int) {
        localDataSource.insertPageCount(pageCount)
    }

    private suspend fun loadPageNumber(): Int {
        return localDataSource.loadPageNumber()
    }

    private suspend fun localSavePageNumberDAO(pageNumber: Int) {
        localDataSource.insertPageNumber(pageNumber)
    }

    companion object {
        private const val PAGE_COUNT_ZERO = 0
        private const val PAGE_COUNT_ONE = 1
    }
}

open class RemoteLoadError(message: String, cause: Throwable) : Throwable(message, cause)
class RemoteLoadMovieListError(message: String, cause: Throwable) : RemoteLoadError(message, cause)
class RemoteLoadPageCountError(message: String, cause: Throwable) : RemoteLoadError(message, cause)