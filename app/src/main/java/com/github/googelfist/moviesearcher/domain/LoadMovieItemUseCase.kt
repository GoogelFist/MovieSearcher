package com.github.googelfist.moviesearcher.domain

import com.github.googelfist.moviesearcher.domain.model.MovieItem
import javax.inject.Inject

class LoadMovieItemUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(id: Int): MovieItem? {
        return repository.loadMovieItem(id)
    }
}