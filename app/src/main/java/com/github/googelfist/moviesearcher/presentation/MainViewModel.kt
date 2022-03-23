package com.github.googelfist.moviesearcher.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.googelfist.moviesearcher.data.RemoteLoadError
import com.github.googelfist.moviesearcher.data.RemoteLoadMovieListError
import com.github.googelfist.moviesearcher.domain.LoadMovieItemUseCase
import com.github.googelfist.moviesearcher.domain.LoadMovieListUseCase
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(
    private val loadMovieListUseCase: LoadMovieListUseCase,
    private val loadMovieItemUseCase: LoadMovieItemUseCase
) : ViewModel() {

    private var _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private var _movieList = MutableLiveData<List<MovieList>>()
    val movieList: LiveData<List<MovieList>>
        get() = _movieList

    private var _movieItem = MutableLiveData<MovieItem?>()
    val movieItem: LiveData<MovieItem?>
        get() = _movieItem

    private var _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading


    fun onLoadMovieList() {
        launchLoadMovieList {
            loadMovieListUseCase()
        }
    }

    fun onLoadMovieItem(id: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val movieDetail = loadMovieItemUseCase.invoke(id)
                _movieItem.value = movieDetail
                _errorMessage.value = null
            } catch (error: Exception) {
                _errorMessage.value = error.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun onClearMovieItemLiveData() {
        _movieItem.value = null
    }

    private fun launchLoadMovieList(block: suspend () -> List<MovieList>): Job {
        return viewModelScope.launch {
            try {
                _loading.value = true
                _movieList.value = block()
                _errorMessage.value = null
            } catch (error: RemoteLoadMovieListError) {
                _errorMessage.value = error.message
            } catch (error: RemoteLoadError) {
                _errorMessage.value = error.message
            } finally {
                _loading.value = false
            }
        }
    }
}