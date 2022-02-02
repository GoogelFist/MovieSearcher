package com.github.googelfist.moviesearcher.data.datasourse.network

import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.data.datasourse.network.model.MovieDTO
import retrofit2.Response
import javax.inject.Inject

class RetrofitDataSourceImpl @Inject constructor() : RemoteDataSource {
    // TODO:
    override suspend fun loadTop250BestFilms(page: Int): Response<MovieDTO> {
        return RetrofitService.getInstance().getMovieList(authorization = AUTH_TOKEN, accept = CONTENT_TYPE , type = TYPE_TOP_250, page = page.toString())
    }

    // TODO:
    override suspend fun loadTop100PopularFilms(page: Int): Response<MovieDTO> {
        return RetrofitService.getInstance().getMovieList(authorization = AUTH_TOKEN,accept = CONTENT_TYPE , type = TYPE_TOP_100, page = page.toString())
    }

    companion object {
        private const val TYPE_TOP_250 = "TOP_250_BEST_FILMS"
        private const val TYPE_TOP_100 = "TOP_100_POPULAR_FILMS"
        private const val PAGE = "1"

        private const val CONTENT_TYPE = "application/json"
        private const val AUTH_TOKEN = "f1702a66-df70-4672-824c-77df35fd6d3f"
    }
}