package com.github.googelfist.moviesearcher.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.googelfist.moviesearcher.data.RemoteLoadMovieItemError
import com.github.googelfist.moviesearcher.data.RemoteLoadMovieListError
import com.github.googelfist.moviesearcher.domain.LoadMovieDetailUseCase
import com.github.googelfist.moviesearcher.domain.LoadPageTop250UseCase
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(
    private val loadFirstPageTop250BestFilmsUseCase: LoadPageTop250UseCase,
    private val loadMovieDetailUseCase: LoadMovieDetailUseCase
) : ViewModel() {

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _movieList = MutableLiveData<List<MovieList>>()
    val movieList: LiveData<List<MovieList>>
        get() = _movieList

    private var _movieDetail = MutableLiveData<MovieItem>()
    val movieItem: LiveData<MovieItem>
        get() = _movieDetail

    private var _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean>
        get() = _loading


    fun onLoadPageTop250BestFilms() {
        launchLoadMovies {
            loadFirstPageTop250BestFilmsUseCase()
        }
    }

    fun onLoadMovieDetail(id: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val movieDetail = loadMovieDetailUseCase.invoke(id)
                _movieDetail.value = movieDetail
            } catch (error: RemoteLoadMovieItemError) {
                _errorMessage.value = error.message
            } finally {
                _loading.value = false
            }
        }
    }

    private fun launchLoadMovies(block: suspend () -> List<MovieList>): Job {
        return viewModelScope.launch {
            try {
                _loading.value = true
                _movieList.value = block()
            } catch (error: RemoteLoadMovieListError) {
                _errorMessage.value = error.message
            } finally {
                _loading.value = false
            }
        }
    }
}