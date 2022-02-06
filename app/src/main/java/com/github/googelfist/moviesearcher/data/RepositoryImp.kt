package com.github.googelfist.moviesearcher.data

import androidx.lifecycle.MutableLiveData
import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.data.mapper.MovieMapper
import com.github.googelfist.moviesearcher.domain.Repository
import com.github.googelfist.moviesearcher.domain.model.MoviePreview
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val mapper: MovieMapper
) : Repository {

    private var pageNumber: Int = PAGE_COUNT_ONE
    private var top250PageCount: Int = PAGE_COUNT_ZERO

    private val previewMovies = mutableListOf<MoviePreview>()

    override suspend fun loadFirstPageTop250BestFilms() : List<MoviePreview> {
        resetPageNumber()
        val movies = loadTop250BestFilms(pageNumber)
        previewMovies.addAll(movies)
        return previewMovies
    }

    override suspend fun loadNextPageTop250BestFilms() : List<MoviePreview> {
        if (pageNumber < top250PageCount) {
            increasePageNumber()
            val movies = loadTop250BestFilms(pageNumber)
            previewMovies.addAll(movies)
        }
        return previewMovies
    }

    private suspend fun loadTop250BestFilms(page: Int): List<MoviePreview> {
        try {
            val result = remoteDataSource.loadTop250BestFilms(page)
            top250PageCount = result.pagesCount
            return mapper.mapMovieDTOtoMoviePreviewList(result)
        } catch (error: Throwable) {
            throw LoadTop250BestFilmsError("Unable to load top 250 best films", error)
        }
    }

    private fun resetPageNumber() {
        pageNumber = PAGE_COUNT_ONE
    }

    private fun increasePageNumber() {
        pageNumber++
    }

    private fun <T> MutableLiveData<List<T>>.updateListValue(list: List<T>) {
        val value = this.value as MutableList<T>
        value.addAll(list)
        this.value = value
    }

    companion object {
        private const val PAGE_COUNT_ZERO = 0
        private const val PAGE_COUNT_ONE = 1
    }
}

class LoadTop250BestFilmsError(message: String, cause: Throwable) : Throwable(message, cause)