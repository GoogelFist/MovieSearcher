package com.github.googelfist.moviesearcher.data.datasourse

import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieItemDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePageListDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.PageCountDAO

interface LocalDataSource {
    suspend fun loadMoviePageList(page: Int): MoviePageListDAO?

    suspend fun insertMoviePageList(moviesPageDAO: MoviePageListDAO)

    suspend fun loadMovieItem(id: Int): MovieItemDAO

    suspend fun insertMovieItem(movieItem: MovieItemDAO)

    suspend fun loadPageCount(): PageCountDAO?

    suspend fun insertPageCount(pageCount: PageCountDAO)
}