package com.github.googelfist.moviesearcher.data.datasourse

import com.github.googelfist.moviesearcher.data.datasourse.network.model.MovieDTO

interface RemoteDataSource {
    suspend fun loadTop250BestFilms(page: Int): MovieDTO
}