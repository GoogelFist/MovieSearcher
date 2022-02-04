package com.github.googelfist.moviesearcher.domain

import com.github.googelfist.moviesearcher.domain.model.MoviePreviewContainer
import javax.inject.Inject

class LoadNextPageTop250UseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(): MoviePreviewContainer {
        return repository.loadNextPageTop250BestFilms()
    }
}