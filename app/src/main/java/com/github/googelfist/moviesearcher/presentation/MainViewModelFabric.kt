package com.github.googelfist.moviesearcher.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.googelfist.moviesearcher.domain.LoadTop250BestFilmsUseCase
import javax.inject.Inject

class MainViewModelFabric @Inject constructor(private val loadTop250BestFilmsUseCase: LoadTop250BestFilmsUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(loadTop250BestFilmsUseCase = loadTop250BestFilmsUseCase) as T
    }
}