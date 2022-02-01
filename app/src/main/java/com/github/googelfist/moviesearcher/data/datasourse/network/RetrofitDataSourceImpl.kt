package com.github.googelfist.moviesearcher.data.datasourse.network

import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.data.datasourse.network.model.MovieDTO
import retrofit2.Response

class RetrofitDataSourceImpl(private val retrofitService: RetrofitService) : RemoteDataSource {
    override suspend fun loadTop250BestFilms(): Response<List<MovieDTO>> {
        return retrofitService.getMovieList(type = TYPE_TOP_250, page = PAGE)
    }

    override suspend fun loadTop100PopularFilms(): Response<List<MovieDTO>> {
        return retrofitService.getMovieList(type = TYPE_TOP_100, page = PAGE)
    }

    companion object {
        private const val TYPE_TOP_250 = "TOP_250_BEST_FILMS"
        private const val TYPE_TOP_100 = "TOP_100_POPULAR_FILMS"
        private const val PAGE = "1"
    }
}