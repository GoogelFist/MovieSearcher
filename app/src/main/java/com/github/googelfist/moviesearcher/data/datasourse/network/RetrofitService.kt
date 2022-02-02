package com.github.googelfist.moviesearcher.data.datasourse.network

import com.github.googelfist.moviesearcher.data.datasourse.network.model.MovieDTO
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    // TODO: path instead query page
    @GET("top")
    suspend fun getMovieList(
        @Header(HEADER_API_KEY) authorization: String,
        @Header(HEADER_CONTENT_TYPE) accept: String,
        @Query(TYPE) type: String,
        @Query(PAGE) page: String
    ): Response<MovieDTO>

    companion object {

        // TODO: to dagger 2
        private const val HEADER_API_KEY = "X-API-KEY"
        private const val HEADER_CONTENT_TYPE = "Content-Type"
        private const val TYPE = "type"
        private const val PAGE = "page"
        private const val BASE_URL = "https://kinopoiskapiunofficial.tech/api/v2.2/films/"

        var retrofitService: RetrofitService? = null
        fun getInstance(): RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}