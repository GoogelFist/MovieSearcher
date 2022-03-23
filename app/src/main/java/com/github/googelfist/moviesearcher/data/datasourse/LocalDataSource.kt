package com.github.googelfist.moviesearcher.data.datasourse

import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieItemDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePageListDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.PageCountDAO
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList

interface LocalDataSource {
    suspend fun loadMoviePageList(page: Int): List<MovieList>?

    suspend fun insertMoviePageList(pageNumber: Int, movieList: List<MovieList>)

    suspend fun loadMovieItem(id: Int): MovieItem?

    suspend fun insertMovieItem(movieItem: MovieItem)

    suspend fun loadPageCount(): Int?

    suspend fun insertPageCount(pageCount: Int)
}