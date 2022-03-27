package com.github.googelfist.moviesearcher.domain

import androidx.lifecycle.LiveData
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import javax.inject.Inject

class LoadMovieItemUseCase @Inject constructor(private val repository: Repository) {

    operator fun invoke(id: Int): LiveData<MovieItem> {
        return repository.loadMovieItem(id)
    }
}