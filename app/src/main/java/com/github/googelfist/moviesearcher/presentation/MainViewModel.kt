package com.github.googelfist.moviesearcher.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.googelfist.moviesearcher.domain.LoadFirstPageTop250UseCase
import com.github.googelfist.moviesearcher.domain.LoadNextPageTop250UseCase
import com.github.googelfist.moviesearcher.domain.model.MoviePreview
import kotlinx.coroutines.launch

class MainViewModel(
    private val loadFirstPageTop250UseCase: LoadFirstPageTop250UseCase,
    private val loadNextPageTop250UseCase: LoadNextPageTop250UseCase
) : ViewModel() {

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private var _movieList = MutableLiveData<List<MoviePreview>>()
    val movieList: LiveData<List<MoviePreview>> = _movieList

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading


    fun onLoadFirstPageTop250BestFilms() {
        viewModelScope.launch {
            val moviesContainer = loadFirstPageTop250UseCase()
            if (moviesContainer.previewMovies.isNotEmpty()) {
                _movieList.postValue(moviesContainer.previewMovies)
                _loading.value = false
            } else {
                onError(moviesContainer.errorMessage)
            }
        }
    }

    fun onLoadNextPageTop250BestFilms() {
        viewModelScope.launch {
            val moviesContainer = loadNextPageTop250UseCase()
            if (moviesContainer.previewMovies.isNotEmpty()) {
                _movieList.postValue(moviesContainer.previewMovies)
                _loading.value = false
            } else {
                onError(moviesContainer.errorMessage)
            }
        }
    }

    private fun onError(message: String) {
        _errorMessage.value = message
        _loading.value = false
    }
}