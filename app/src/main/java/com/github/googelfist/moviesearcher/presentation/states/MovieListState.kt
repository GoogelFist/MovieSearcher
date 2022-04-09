package com.github.googelfist.moviesearcher.presentation.states

sealed class MovieListState {
    object LoadingState : MovieListState()
    object LoadedListState : MovieListState()

    object NoListState : MovieListState()
    class ErrorState(val message: String): MovieListState()

    object UpdatingState : MovieListState()
    object UpdatedState : MovieListState()
}