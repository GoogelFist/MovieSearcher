package com.github.googelfist.moviesearcher.domain

import com.github.googelfist.moviesearcher.data.datasourse.network.model.MovieDTO
import retrofit2.Response

interface Repository {
    suspend fun loadTop250BestFilms(): Response<List<MovieDTO>>

    suspend fun loadTop100PopularFilms(): Response<List<MovieDTO>>
}