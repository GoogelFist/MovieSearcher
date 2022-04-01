package com.github.googelfist.moviesearcher.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.googelfist.moviesearcher.data.RemoteLoadMovieItemError
import com.github.googelfist.moviesearcher.data.RemoteLoadMovieListError
import com.github.googelfist.moviesearcher.data.RemoteLoadPageCountError
import com.github.googelfist.moviesearcher.domain.LoadMovieItemUseCase
import com.github.googelfist.moviesearcher.domain.LoadMovieListUseCase
import com.github.googelfist.moviesearcher.domain.UpdateMovieItemUseCase
import com.github.googelfist.moviesearcher.domain.UpdateMovieListUseCase
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(
    private val loadMovieListUseCase: LoadMovieListUseCase,
    private val loadMovieItemUseCase: LoadMovieItemUseCase,
    private val updateMovieListUseCase: UpdateMovieListUseCase,
    private val updateMovieItemUseCase: UpdateMovieItemUseCase
) : ViewModel() {

    private var _snackBar = MutableLiveData<String?>()
    val snackBar: LiveData<String?>
        get() = _snackBar

    lateinit var movieList: LiveData<List<MovieList>>

    lateinit var movieItem: LiveData<MovieItem>

    private var _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    fun onUpdateMovieList() {
        launchUpdateMovieList {
            updateMovieListUseCase()
        }
    }

    fun onUpdateMovieItem(id: Int) {
        launchUpdateMovieItem() {
            updateMovieItemUseCase(id)
        }
    }

    fun onLoadMovieList() {
        movieList = loadMovieListUseCase()
    }

    fun onLoadMovieItem(id: Int) {
        movieItem = loadMovieItemUseCase(id)
    }

    fun onSnackBarShown() {
        _snackBar.value = null
    }

    private fun launchUpdateMovieList(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _loading.value = true
                block()
            } catch (error: Throwable) {
                when (error) {
                    is RemoteLoadMovieListError, is RemoteLoadPageCountError -> {
                        _snackBar.value = error.message
                    }
                }
            } finally {
                _loading.value = false
            }
        }
    }

    private fun launchUpdateMovieItem(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _loading.value = true
                block()
            } catch (error: RemoteLoadMovieItemError) {
                _snackBar.value = error.message
            } finally {
                _loading.value = false
            }
        }
    }
}