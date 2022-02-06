package com.github.googelfist.moviesearcher.domain

import androidx.lifecycle.LiveData
import com.github.googelfist.moviesearcher.domain.model.MoviePreview

interface Repository {
    suspend fun loadFirstPageTop250BestFilms(): List<MoviePreview>

    suspend fun loadNextPageTop250BestFilms(): List<MoviePreview>
}