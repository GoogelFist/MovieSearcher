package com.github.googelfist.moviesearcher.domain

import com.github.googelfist.moviesearcher.domain.model.MovieList
import javax.inject.Inject

class LoadMovieListUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(): List<MovieList> {
        return repository.loadMovieList()
    }
}