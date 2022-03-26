package com.github.googelfist.moviesearcher.data.datasourse

import androidx.lifecycle.LiveData
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieListDAO
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList

interface LocalDataSource {
    fun loadAllMovieLists(): LiveData<List<MovieList>>

    suspend fun insertMoviePageList(pageNumber: Int, movieList: List<MovieList>)

    suspend fun loadMovieItem(id: Int): MovieItem?

    suspend fun insertMovieItem(movieItem: MovieItem)

    suspend fun loadPageCount(): Int

    suspend fun insertPageCount(pageCount: Int)

    suspend fun loadPageNumber(): Int

    suspend fun insertPageNumber(pageNumber: Int)
}