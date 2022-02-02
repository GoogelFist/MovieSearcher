package com.github.googelfist.moviesearcher.data

import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.data.datasourse.network.model.MovieDTO
import com.github.googelfist.moviesearcher.data.mapper.MovieMapper
import com.github.googelfist.moviesearcher.domain.Repository
import com.github.googelfist.moviesearcher.domain.model.MoviePreviewContainer
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val mapper: MovieMapper
) : Repository {

    private var pageNumber: Int = PAGE_COUNT_ONE
    private var top250PageCount: Int = PAGE_COUNT_ZERO

    override suspend fun loadFirstPageTop250BestFilms(): MoviePreviewContainer {
        return loadTop250BestFilms(PAGE_COUNT_ONE)
    }


    override suspend fun loadNextPageTop250BestFilms(): MoviePreviewContainer {
        return if (pageNumber < top250PageCount) {
            increasePageNumber()
            loadTop100PopularFilms(pageNumber)
        } else {
            // TODO: need optimize this logic
            loadFirstPageTop250BestFilms()
        }
    }

    override suspend fun loadTop100PopularFilms(page: Int): MoviePreviewContainer {
//        return remoteDataSource.loadTop100PopularFilms(page)
        TODO()
    }

    private suspend fun loadTop250BestFilms(page: Int): MoviePreviewContainer {
        val response = remoteDataSource.loadTop250BestFilms(page)
        if (response.isSuccessful) {
            if (response.body() == null) {
                return MoviePreviewContainer(
                    errorMessage = ERROR_MESSAGE,
                    previewMovies = emptyList()
                )
            }
            val dtoList = response.body()
            val previewMovieList = mapper.mapMovieDTOtoMoviePreviewList(dtoList as MovieDTO)
            top250PageCount = dtoList.pagesCount
            return MoviePreviewContainer(
                errorMessage = EMPTY_STRING,
                previewMovies = previewMovieList
            )
        }
        return MoviePreviewContainer(
            errorMessage = "$NOT_SUCCESSFUL_ERROR_MESSAGE ${response.message()}",
            previewMovies = emptyList()
        )
    }

    private fun increasePageNumber() {
        pageNumber++
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val ERROR_MESSAGE = "Response body is null"
        private const val NOT_SUCCESSFUL_ERROR_MESSAGE = "Not successful error:"
        private const val PAGE_COUNT_ZERO = 0
        private const val PAGE_COUNT_ONE = 1

    }
}