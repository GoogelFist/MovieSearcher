package com.github.googelfist.moviesearcher.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.googelfist.moviesearcher.data.RemoteLoadMovieItemError
import com.github.googelfist.moviesearcher.domain.LoadMovieItemUseCase
import com.github.googelfist.moviesearcher.domain.UpdateMovieItemUseCase
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.presentation.states.MovieItemState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ViewModelMovieItem(
    private val loadMovieItemUseCase: LoadMovieItemUseCase,
    private val updateMovieItemUseCase: UpdateMovieItemUseCase
) : ViewModel() {

    private var _movieItemState = MutableLiveData<MovieItemState>()
    val movieItemState: LiveData<MovieItemState>
        get() = _movieItemState

    private var _movieItem = MutableLiveData<MovieItem?>()
    val movieItem: LiveData<MovieItem?>
        get() = _movieItem


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

    private fun launchLoadMovieItem(block: suspend () -> MovieItem?): Job {
        return viewModelScope.launch {
            val movieItem = block()

            if (movieItem == null) {
                _movieItemState.value = MovieItemState.NoItemState
            } else {
                _movieItem.value = movieItem
                _movieItemState.value = MovieItemState.SuccessState
            }
        }
    }

    private fun launchUpdateMovieItem(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _movieItem.value = null
                _movieItemState.value = MovieItemState.UpdatingState
                block()
                _movieItemState.value = MovieItemState.SuccessState
            } catch (error: Throwable) {
                if (error is RemoteLoadMovieItemError) {
                    _movieItemState.value =
                        MovieItemState.ErrorState(UPDATE_MOVIE_ITEM_ERROR_MESSAGE)
                } else {
                    _movieItemState.value = MovieItemState.ErrorState(error.message.toString())
                }
            }
        }
    }

    companion object {
        private const val UPDATE_MOVIE_ITEM_ERROR_MESSAGE = "Unable to load movie item"
    }
}