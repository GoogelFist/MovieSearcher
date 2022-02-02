package com.github.googelfist.moviesearcher.domain

import com.github.googelfist.moviesearcher.domain.model.MoviePreviewContainer
import javax.inject.Inject

class LoadTop250BestFilmsUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(page: Int): MoviePreviewContainer {
        return repository.loadTop250BestFilms(page)
    }
}