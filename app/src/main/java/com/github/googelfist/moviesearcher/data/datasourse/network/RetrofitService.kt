package com.github.googelfist.moviesearcher.data.datasourse.network

import com.github.googelfist.moviesearcher.data.datasourse.network.model.detail.MovieItemDTO
import com.github.googelfist.moviesearcher.data.datasourse.network.model.preview.MovieListDTO
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {

    @GET("top")
    suspend fun getMovieList(
        @Header(HEADER_API_KEY) authToken: String,
        @Header(HEADER_CONTENT_TYPE) contentType: String,
        @Query(TYPE) type: String,
        @Query(PAGE) page: String
    ): MovieListDTO

    @GET("{id}")
    suspend fun getMovie(
        @Header(HEADER_API_KEY) authToken: String,
        @Header(HEADER_CONTENT_TYPE) contentType: String,
        @Path(PATH) id: Int
    ): MovieItemDTO

    companion object {
        private const val HEADER_API_KEY = "X-API-KEY"
        private const val HEADER_CONTENT_TYPE = "Content-Type"
        private const val PATH = "id"
        private const val TYPE = "type"
        private const val PAGE = "page"
    }
}