package com.github.googelfist.moviesearcher.domain

import com.github.googelfist.moviesearcher.domain.model.MoviePreviewContainer

interface Repository {
    suspend fun loadFirstPageTop250BestFilms(): MoviePreviewContainer

    suspend fun loadNextPageTop250BestFilms(): MoviePreviewContainer
}