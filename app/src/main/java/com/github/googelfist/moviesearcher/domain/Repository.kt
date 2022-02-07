package com.github.googelfist.moviesearcher.domain

import com.github.googelfist.moviesearcher.domain.model.MovieDetail
import com.github.googelfist.moviesearcher.domain.model.MoviePreview

interface Repository {
    suspend fun loadFirstPageTop250BestFilms(): List<MoviePreview>

    suspend fun loadNextPageTop250BestFilms(): List<MoviePreview>

    suspend fun loadMovieDetail(id: Int): MovieDetail
}