package com.github.googelfist.moviesearcher.data.datasourse

import com.github.googelfist.moviesearcher.data.datasourse.network.model.MovieDTO
import retrofit2.Response

interface RemoteDataSource {
    suspend fun loadTop250BestFilms(): Response<List<MovieDTO>>

    suspend fun loadTop100PopularFilms(): Response<List<MovieDTO>>
}