package com.github.googelfist.moviesearcher.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.googelfist.moviesearcher.domain.LoadMovieItemUseCase
import com.github.googelfist.moviesearcher.domain.UpdateMovieItemUseCase
import javax.inject.Inject

class ViewModelMovieItemFabric @Inject constructor(
    private val loadMovieItemUseCase: LoadMovieItemUseCase,
    private val updateMovieItemUseCase: UpdateMovieItemUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelMovieItem::class.java)) {
            return ViewModelMovieItem(
                loadMovieItemUseCase = loadMovieItemUseCase,
                updateMovieItemUseCase = updateMovieItemUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}