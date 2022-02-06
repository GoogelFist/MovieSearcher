package com.github.googelfist.moviesearcher.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.googelfist.moviesearcher.data.LoadTop250BestFilmsError
import com.github.googelfist.moviesearcher.domain.LoadFirstPageTop250UseCase
import com.github.googelfist.moviesearcher.domain.LoadNextPageTop250UseCase
import com.github.googelfist.moviesearcher.domain.model.MoviePreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(
    private val loadFirstPageTop250UseCase: LoadFirstPageTop250UseCase,
    private val loadNextPageTop250UseCase: LoadNextPageTop250UseCase
) : ViewModel() {

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _movieList = MutableLiveData<List<MoviePreview>>()
    val movieList: LiveData<List<MoviePreview>>
        get() = _movieList

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading


    fun onLoadFirstPageTop250BestFilms() {
        launchLoadMovies {
            loadFirstPageTop250UseCase()
        }
    }

    fun onLoadNextPageTop250BestFilms() {
        launchLoadMovies {
            loadNextPageTop250UseCase()
        }
    }

    private fun launchLoadMovies(block: suspend () -> List<MoviePreview>) : Job {
        return viewModelScope.launch {
            try {
                _movieList.value = block()
                _loading.value = false
            } catch (error: LoadTop250BestFilmsError) {
                error.message?.let { onError(it) }
            }
        }
    }

    private fun onError(message: String) {
        _errorMessage.value = message
        _loading.value = false
    }
}