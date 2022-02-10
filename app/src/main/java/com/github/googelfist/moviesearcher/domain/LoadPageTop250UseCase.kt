package com.github.googelfist.moviesearcher.domain

import com.github.googelfist.moviesearcher.domain.model.MovieList
import javax.inject.Inject

class LoadPageTop250UseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(): List<MovieList> {
        return repository.loadPageTop250BestFilms()
    }
}