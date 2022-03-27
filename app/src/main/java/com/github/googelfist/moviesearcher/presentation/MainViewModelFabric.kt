package com.github.googelfist.moviesearcher.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.googelfist.moviesearcher.domain.LoadMovieItemUseCase
import com.github.googelfist.moviesearcher.domain.LoadMovieListUseCase
import com.github.googelfist.moviesearcher.domain.UpdateMovieItemUseCase
import com.github.googelfist.moviesearcher.domain.UpdateMovieListUseCase
import javax.inject.Inject

class MainViewModelFabric @Inject constructor(
    private val loadMovieListUseCase: LoadMovieListUseCase,
    private val loadMovieItemUseCase: LoadMovieItemUseCase,
    private val updateMovieListUseCase: UpdateMovieListUseCase,
    private val updateMovieItemUseCase: UpdateMovieItemUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                loadMovieListUseCase = loadMovieListUseCase,
                loadMovieItemUseCase = loadMovieItemUseCase,
                updateMovieListUseCase = updateMovieListUseCase,
                updateMovieItemUseCase = updateMovieItemUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}