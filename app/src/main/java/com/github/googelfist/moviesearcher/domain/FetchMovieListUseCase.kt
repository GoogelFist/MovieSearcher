package com.github.googelfist.moviesearcher.domain

import javax.inject.Inject

class FetchMovieListUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke() {
        repository.fetchMovieList()
    }
}