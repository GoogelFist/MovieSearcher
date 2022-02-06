package com.github.googelfist.moviesearcher.domain

import androidx.lifecycle.LiveData
import com.github.googelfist.moviesearcher.domain.model.MoviePreview
import javax.inject.Inject

class LoadFirstPageTop250UseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(): List<MoviePreview> {
        return repository.loadFirstPageTop250BestFilms()
    }
}