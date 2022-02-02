package com.github.googelfist.moviesearcher.domain

import com.github.googelfist.moviesearcher.domain.model.MoviePreview
import com.github.googelfist.moviesearcher.domain.model.MoviePreviewContainer
import retrofit2.Response

interface Repository {
    suspend fun loadFirstPageTop250BestFilms(): MoviePreviewContainer

    suspend fun loadNextPageTop250BestFilms(): MoviePreviewContainer

    suspend fun loadTop100PopularFilms(page: Int): MoviePreviewContainer
}