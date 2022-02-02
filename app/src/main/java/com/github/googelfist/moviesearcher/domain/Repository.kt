package com.github.googelfist.moviesearcher.domain

import com.github.googelfist.moviesearcher.data.datasourse.network.model.ResponseMovieDTO
import retrofit2.Response

interface Repository {
    suspend fun loadTop250BestFilms(page: Int): Response<ResponseMovieDTO>

    suspend fun loadTop100PopularFilms(page: Int): Response<ResponseMovieDTO>
}