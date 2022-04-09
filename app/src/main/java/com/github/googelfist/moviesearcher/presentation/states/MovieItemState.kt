package com.github.googelfist.moviesearcher.presentation.states

sealed class MovieItemState {
    object LoadingState : MovieItemState()
    object LoadedItemState : MovieItemState()

    object NoItemState : MovieItemState()
    class ErrorState(val message: String): MovieItemState()

    object UpdatingState : MovieItemState()
    object UpdatedState : MovieItemState()
}