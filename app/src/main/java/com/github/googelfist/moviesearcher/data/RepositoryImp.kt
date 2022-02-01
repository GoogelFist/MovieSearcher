package com.github.googelfist.moviesearcher.data

import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.data.datasourse.network.model.MovieDTO
import com.github.googelfist.moviesearcher.domain.Repository
import retrofit2.Response

class RepositoryImp(private val remoteDataSource: RemoteDataSource) : Repository {
    override suspend fun loadTop250BestFilms(): Response<List<MovieDTO>> {
        return remoteDataSource.loadTop250BestFilms()
    }

    override suspend fun loadTop100PopularFilms(): Response<List<MovieDTO>> {
        return remoteDataSource.loadTop100PopularFilms()
    }
}