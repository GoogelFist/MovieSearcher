package com.github.googelfist.moviesearcher.domain

import com.github.googelfist.moviesearcher.domain.model.MoviePreview
import javax.inject.Inject

class LoadPageTop250UseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(): List<MoviePreview> {
        return repository.loadPageTop250BestFilms()
    }
}