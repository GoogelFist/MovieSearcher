package com.github.googelfist.moviesearcher.data.datasourse

import com.github.googelfist.moviesearcher.data.datasourse.network.model.ResponseMovieDTO
import retrofit2.Response

interface RemoteDataSource {
    suspend fun loadTop250BestFilms(page: Int): Response<ResponseMovieDTO>

    suspend fun loadTop100PopularFilms(page: Int): Response<ResponseMovieDTO>
}