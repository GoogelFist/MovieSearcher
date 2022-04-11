package com.github.googelfist.moviesearcher.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.googelfist.moviesearcher.domain.LoadMovieListUseCase
import com.github.googelfist.moviesearcher.domain.UpdateMovieListUseCase
import javax.inject.Inject

class ViewModelMovieListFabric @Inject constructor(
    private val loadMovieListUseCase: LoadMovieListUseCase,
    private val updateMovieListUseCase: UpdateMovieListUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelMovieList::class.java)) {
            return ViewModelMovieList(
                loadMovieListUseCase = loadMovieListUseCase,
                updateMovieListUseCase = updateMovieListUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}