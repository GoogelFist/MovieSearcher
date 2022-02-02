package com.github.googelfist.moviesearcher.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.googelfist.moviesearcher.domain.Repository
import javax.inject.Inject

class MainViewModelFabric @Inject constructor(private val repository: Repository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository = repository) as T
    }
}