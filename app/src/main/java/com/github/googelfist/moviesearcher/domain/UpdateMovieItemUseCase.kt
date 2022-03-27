package com.github.googelfist.moviesearcher.domain

import javax.inject.Inject

class UpdateMovieItemUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(id: Int) {
        repository.updateMovieItem(id)
    }
}