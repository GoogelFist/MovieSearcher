package com.github.googelfist.moviesearcher.domain

import androidx.lifecycle.LiveData
import com.github.googelfist.moviesearcher.domain.model.MovieList
import javax.inject.Inject

class LoadMovieListUseCase @Inject constructor(private val repository: Repository) {

    operator fun invoke(): LiveData<List<MovieList>> {
        return repository.loadMovieList()
    }
}