package com.github.googelfist.moviesearcher.data.datasourse.network

import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.data.datasourse.network.model.detail.MovieDetailDTO
import com.github.googelfist.moviesearcher.data.datasourse.network.model.preview.MoviePreviewDTO
import javax.inject.Inject

class RetrofitDataSourceImpl @Inject constructor(private val retrofitService: RetrofitService) :
    RemoteDataSource {
    override suspend fun loadTop250BestFilms(page: Int): MoviePreviewDTO {
        return retrofitService.getMovieList(
            authToken = AUTH_TOKEN,
            contentType = CONTENT_TYPE,
            type = TYPE_TOP_250,
            page = page.toString()
        )
    }

    override suspend fun loadMovieDetail(id: Int): MovieDetailDTO {
        return retrofitService.getMovie(
            authToken = AUTH_TOKEN,
            contentType = CONTENT_TYPE,
            id = id
        )
    }

    companion object {
        private const val TYPE_TOP_250 = "TOP_250_BEST_FILMS"

        private const val CONTENT_TYPE = "application/json"
        private const val AUTH_TOKEN = "f1702a66-df70-4672-824c-77df35fd6d3f"
    }
}