package com.github.googelfist.moviesearcher.data.datasourse.network

import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.data.datasourse.network.model.list.MovieListDTO
import com.github.googelfist.moviesearcher.data.mapper.MovieMapper
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList
import javax.inject.Inject

class RetrofitDataSourceImpl @Inject constructor(
    private val retrofitService: RetrofitService,
    private val mapper: MovieMapper
) :
    RemoteDataSource {
    override suspend fun loadMovieList(page: Int): List<MovieList> {
        val movieListDTO = loadMovieListDTO(page)
        return mapper.mapMovieListDTOtoMovieList(movieListDTO)
    }

    override suspend fun loadMovieItem(id: Int): MovieItem {
        val movieItemDTO = retrofitService.getMovie(
            authToken = AUTH_TOKEN,
            contentType = CONTENT_TYPE,
            id = id
        )
        return mapper.mapMovieItemDTOtoMovieItem(movieItemDTO)
    }

    override suspend fun loadPageCount(page: Int): Int {
        val movieListDTO = loadMovieListDTO(page)
        return movieListDTO.pagesCount
    }

    private suspend fun loadMovieListDTO(page: Int): MovieListDTO {
        return retrofitService.getMovieList(
            authToken = AUTH_TOKEN,
            contentType = CONTENT_TYPE,
            type = TYPE_TOP_250,
            page = page.toString()
        )
    }

    companion object {
        private const val TYPE_TOP_250 = "TOP_250_BEST_FILMS"

        private const val CONTENT_TYPE = "application/json"
        private const val AUTH_TOKEN = "f1702a66-df70-4672-824c-77df35fd6d3f"
    }
}