package com.github.googelfist.moviesearcher.data

import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.data.datasourse.network.model.MovieDTO
import com.github.googelfist.moviesearcher.domain.Repository
import com.github.googelfist.moviesearcher.domain.mapper.MovieMapper
import com.github.googelfist.moviesearcher.domain.model.MoviePreviewContainer
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val mapper: MovieMapper
) : Repository {

    override suspend fun loadTop250BestFilms(page: Int): MoviePreviewContainer {
        val response = remoteDataSource.loadTop250BestFilms(page)
        if (response.isSuccessful) {
            if (response.body() == null) {
                return MoviePreviewContainer(ERROR_MESSAGE, emptyList())
            }
            val dtoList = response.body()
            val previewMovieList = mapper.mapMovieDTOtoMoviePreviewList(dtoList as MovieDTO)
            return MoviePreviewContainer(EMPTY_STRING, previewMovieList)
        }
        return MoviePreviewContainer("$NOT_SUCCESSFUL_ERROR_MESSAGE ${response.message()}", emptyList())
    }

    override suspend fun loadTop100PopularFilms(page: Int): MoviePreviewContainer {
//        return remoteDataSource.loadTop100PopularFilms(page)
        TODO()
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val ERROR_MESSAGE = "Response body is null"
        private const val NOT_SUCCESSFUL_ERROR_MESSAGE = "Not successful error:"
    }
}