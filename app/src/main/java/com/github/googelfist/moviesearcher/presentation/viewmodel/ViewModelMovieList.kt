package com.github.googelfist.moviesearcher.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.googelfist.moviesearcher.data.RemoteLoadMovieListError
import com.github.googelfist.moviesearcher.data.RemoteLoadPageCountError
import com.github.googelfist.moviesearcher.domain.LoadMovieListUseCase
import com.github.googelfist.moviesearcher.domain.UpdateMovieListUseCase
import com.github.googelfist.moviesearcher.domain.model.MovieList
import com.github.googelfist.moviesearcher.presentation.states.MovieListState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ViewModelMovieList(
    private val loadMovieListUseCase: LoadMovieListUseCase,
    private val updateMovieListUseCase: UpdateMovieListUseCase
) : ViewModel() {

    private var _listState = MutableLiveData<MovieListState>()
    val movieListState: LiveData<MovieListState>
        get() = _listState

    private var _movieList = MutableLiveData<List<MovieList>>()
    val movieList: LiveData<List<MovieList>>
        get() = _movieList

    init {
        initMovieList()
    }

    fun onRefreshList() {
        viewModelScope.launch {
            launchUpdateMovieList {
                updateMovieListUseCase()
            }.join()

            launchLoadMovieList {
                loadMovieListUseCase()
            }.join()
        }
    }

    private fun initMovieList() {
        viewModelScope.launch {
            val movieList = loadMovieListUseCase()
            if (movieList.isEmpty()) {
                onRefreshList()
            } else {
                launchLoadMovieList {
                    loadMovieListUseCase()
                }
            }
        }
    }

    private fun launchLoadMovieList(block: suspend () -> List<MovieList>): Job {
        return viewModelScope.launch {
            val movieList = block()

            if (movieList.isEmpty()) {
                _listState.value = MovieListState.NoListState
            } else {
                _movieList.value = movieList
                _listState.value = MovieListState.SuccessState
            }
        }
    }

    private fun launchUpdateMovieList(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _listState.value = MovieListState.UpdatingState
                block()
                _listState.value = MovieListState.SuccessState
            } catch (error: Throwable) {
                when (error) {
                    is RemoteLoadMovieListError, is RemoteLoadPageCountError -> {
                        _listState.value =
                            MovieListState.ErrorState(UPDATE_MOVIE_LIST_ERROR_MESSAGE)
                    }
                    else -> {
                        _listState.value = MovieListState.ErrorState(error.message.toString())
                    }
                }
            }
        }
    }

    companion object {
        private const val UPDATE_MOVIE_LIST_ERROR_MESSAGE = "Unable to load movie list"
    }
}