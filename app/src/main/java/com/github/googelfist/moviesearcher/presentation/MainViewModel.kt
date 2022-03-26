package com.github.googelfist.moviesearcher.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.googelfist.moviesearcher.data.RemoteLoadError
import com.github.googelfist.moviesearcher.data.RemoteLoadMovieListError
import com.github.googelfist.moviesearcher.domain.LoadMovieItemUseCase
import com.github.googelfist.moviesearcher.domain.LoadMovieListUseCase
import com.github.googelfist.moviesearcher.domain.FetchMovieListUseCase
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(
    private val loadMovieListUseCase: LoadMovieListUseCase,
    private val loadMovieItemUseCase: LoadMovieItemUseCase,
    private val fetchMovieListUseCase: FetchMovieListUseCase
) : ViewModel() {

    private var _snackBar = MutableLiveData<String?>()
    val snackBar: LiveData<String?>
        get() = _snackBar

    var movieList = loadMovieListUseCase()

    private var _movieItem = MutableLiveData<MovieItem?>()
    val movieItem: LiveData<MovieItem?>
        get() = _movieItem

    private var _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    fun onFetchMovieList() {
        launchFetchMovieList {
            fetchMovieListUseCase()
        }
    }

//    fun onLoadMovieItem(id: Int) {
//        viewModelScope.launch {
//            try {
//                _loading.value = true
//                val movieDetail = loadMovieItemUseCase.invoke(id)
//                _movieItem.value = movieDetail
////                _errorMessage.value = null
//            } catch (error: Exception) {
//                _snackBar.value = error.message
//            } finally {
//                _loading.value = false
//            }
//        }
//    }

    fun onSnackBarShown() {
        _snackBar.value = null
    }

//    fun onClearMovieItemLiveData() {
//        _movieItem.value = null
//    }

    private fun launchFetchMovieList(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _loading.value = true
                block()
            } catch (error: RemoteLoadMovieListError) {
                _snackBar.value = error.message
            } catch (error: RemoteLoadError) {
                _snackBar.value = error.message
            } finally {
                _loading.value = false
            }
        }
    }
}