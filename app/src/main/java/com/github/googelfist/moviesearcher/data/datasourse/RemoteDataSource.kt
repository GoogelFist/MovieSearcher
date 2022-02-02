package com.github.googelfist.moviesearcher.data.datasourse

import com.github.googelfist.moviesearcher.data.datasourse.network.model.MovieDTO
import retrofit2.Response

interface RemoteDataSource {
    suspend fun loadTop250BestFilms(page: Int): Response<MovieDTO>

    suspend fun loadTop100PopularFilms(page: Int): Response<MovieDTO>
}