package com.github.googelfist.moviesearcher.domain

import com.github.googelfist.moviesearcher.domain.model.MovieDetail
import com.github.googelfist.moviesearcher.domain.model.MoviePreview

interface Repository {
    suspend fun loadPageTop250BestFilms(): List<MoviePreview>

    suspend fun loadMovieDetail(id: Int): MovieDetail
}