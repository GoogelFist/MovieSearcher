package com.github.googelfist.moviesearcher.domain

import com.github.googelfist.moviesearcher.domain.model.MovieDetail
import javax.inject.Inject

class LoadMovieDetailUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(id: Int): MovieDetail {
        return repository.loadMovieDetail(id)
    }
}