package com.github.googelfist.moviesearcher.presentation.states

sealed class MovieListState {
    object NoListState : MovieListState()
    data class ErrorState(val message: String): MovieListState()

    object UpdatingState : MovieListState()
    object SuccessState : MovieListState()
}