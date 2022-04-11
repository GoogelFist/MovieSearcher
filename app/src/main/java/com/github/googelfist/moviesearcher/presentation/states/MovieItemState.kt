package com.github.googelfist.moviesearcher.presentation.states

sealed class MovieItemState {
    object NoItemState : MovieItemState()
    data class ErrorState(val message: String): MovieItemState()

    object UpdatingState : MovieItemState()
    object SuccessState : MovieItemState()
}