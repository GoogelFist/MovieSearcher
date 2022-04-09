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
import com.github.googelfist.moviesearcher.presentation.states.MovieItemState
import com.github.googelfist.moviesearcher.presentation.states.MovieListState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(
    private val loadMovieListUseCase: LoadMovieListUseCase,
    private val loadMovieItemUseCase: LoadMovieItemUseCase,
    private val updateMovieListUseCase: UpdateMovieListUseCase,
    private val updateMovieItemUseCase: UpdateMovieItemUseCase
) : ViewModel() {

    private var _listState = MutableLiveData<MovieListState>()
    val movieListState: LiveData<MovieListState>
        get() = _listState

    private var _itemState = MutableLiveData<MovieItemState>()
    val movieItemState: LiveData<MovieItemState>
        get() = _itemState

    private var _movieList = MutableLiveData<List<MovieList>>()
    val movieList: LiveData<List<MovieList>>
        get() = _movieList

    private var _movieItem = MutableLiveData<MovieItem?>()
    val movieItem: LiveData<MovieItem?>
        get() = _movieItem


    init {
        onRefreshList()
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

    fun onRefreshItem(id: Int) {
        viewModelScope.launch {
            launchUpdateMovieItem {
                updateMovieItemUseCase(id)
            }.join()

            launchLoadMovieItem {
                loadMovieItemUseCase(id)
            }.join()
        }
    }

    private fun launchLoadMovieList(block: suspend () -> List<MovieList>): Job {
        return viewModelScope.launch {
            _listState.value = MovieListState.LoadingState
            val movieList = block()

            if (movieList.isEmpty()) {
                _listState.value = MovieListState.NoListState
            } else {
                _movieList.value = movieList
                _listState.value = MovieListState.LoadedListState
            }
        }
    }

    private fun launchUpdateMovieList(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _listState.value = MovieListState.UpdatingState
                block()
                _listState.value = MovieListState.UpdatedState
            } catch (error: Throwable) {
                when (error) {
                    is RemoteLoadMovieListError, is RemoteLoadPageCountError -> {
                        _listState.value = MovieListState.ErrorState(UPDATE_MOVIE_LIST_ERROR_MESSAGE)
                    }
                    else -> {
                        _listState.value = MovieListState.ErrorState(error.message.toString())
                    }
                }
            }
        }
    }

    private fun launchLoadMovieItem(block: suspend () -> MovieItem?): Job {
        return viewModelScope.launch {
            _itemState.value = MovieItemState.LoadingState
            val movieItem = block()

            if (movieItem == null) {
                _itemState.value = MovieItemState.NoItemState
            } else {
                _movieItem.value = movieItem
                _itemState.value = MovieItemState.LoadedItemState
            }
        }
    }

    private fun launchUpdateMovieItem(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _itemState.value = MovieItemState.UpdatingState
                block()
                _itemState.value = MovieItemState.UpdatedState
            } catch (error: Throwable) {
                if (error is RemoteLoadMovieItemError) {
                    _itemState.value = MovieItemState.ErrorState(UPDATE_MOVIE_ITEM_ERROR_MESSAGE)
                } else {
                    _itemState.value = MovieItemState.ErrorState(error.message.toString())
                }
            }
        }
    }

    companion object {
        private const val UPDATE_MOVIE_LIST_ERROR_MESSAGE = "Unable to load movie list"
        private const val UPDATE_MOVIE_ITEM_ERROR_MESSAGE = "Unable to load movie item"
    }
}