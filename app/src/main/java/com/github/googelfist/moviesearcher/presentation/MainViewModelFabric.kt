package com.github.googelfist.moviesearcher.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.googelfist.moviesearcher.domain.LoadFirstPageTop250UseCase
import java.lang.IllegalArgumentException
import javax.inject.Inject

class MainViewModelFabric @Inject constructor(private val loadFirstPageTop250UseCase: LoadFirstPageTop250UseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(loadFirstPageTop250UseCase = loadFirstPageTop250UseCase) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}