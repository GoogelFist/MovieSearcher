package com.github.googelfist.moviesearcher.data.datasourse.network

import com.github.googelfist.moviesearcher.data.datasourse.network.model.MovieDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {

    @GET("top")
    suspend fun getMovieList(
        @Header(HEADER_API_KEY) authToken: String,
        @Header(HEADER_CONTENT_TYPE) contentType: String,
        @Query(TYPE) type: String,
        @Query(PAGE) page: String
    ): Response<MovieDTO>

    companion object {
        private const val HEADER_API_KEY = "X-API-KEY"
        private const val HEADER_CONTENT_TYPE = "Content-Type"
        private const val TYPE = "type"
        private const val PAGE = "page"
    }
}