package com.github.googelfist.moviesearcher.data

import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.data.datasourse.network.model.ResponseMovieDTO
import com.github.googelfist.moviesearcher.domain.Repository
import retrofit2.Response
import javax.inject.Inject

class RepositoryImp @Inject constructor(private val remoteDataSource: RemoteDataSource) : Repository {
    override suspend fun loadTop250BestFilms(page: Int): Response<ResponseMovieDTO> {
        return remoteDataSource.loadTop250BestFilms(page)
    }

    override suspend fun loadTop100PopularFilms(page: Int): Response<ResponseMovieDTO> {
        return remoteDataSource.loadTop100PopularFilms(page)
    }
}