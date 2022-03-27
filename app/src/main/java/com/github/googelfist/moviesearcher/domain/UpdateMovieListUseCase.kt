package com.github.googelfist.moviesearcher.domain

import javax.inject.Inject

class UpdateMovieListUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke() {
        repository.updateMovieList()
    }
}