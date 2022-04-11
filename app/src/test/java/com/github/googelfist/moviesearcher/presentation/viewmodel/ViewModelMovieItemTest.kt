package com.github.googelfist.moviesearcher.presentation.viewmodel

import com.github.googelfist.moviesearcher.data.RemoteLoadMovieItemError
import com.github.googelfist.moviesearcher.domain.LoadMovieItemUseCase
import com.github.googelfist.moviesearcher.domain.UpdateMovieItemUseCase
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList
import com.github.googelfist.moviesearcher.presentation.states.MovieItemState
import com.github.googelfist.moviesearcher.utils.CoroutinesTestExtension
import com.github.googelfist.moviesearcher.utils.InstantTaskExecutorExtension
import com.github.googelfist.moviesearcher.utils.captureValues
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.kotlin.mock

internal class ViewModelMovieItemTest {

    private val loadMovieItemUseCase = mock<LoadMovieItemUseCase>()
    private val updateMovieItemUseCase = mock<UpdateMovieItemUseCase>()

    private lateinit var viewModelMovieItem: ViewModelMovieItem
    private var expectedMovieItem: MovieItem? = null
    private lateinit var expectedMovieList: List<MovieList>


    @BeforeEach
    fun setUp() {

        viewModelMovieItem = ViewModelMovieItem(loadMovieItemUseCase, updateMovieItemUseCase)

        expectedMovieItem = MovieItem(
            1,
            "Batman",
            "Batman",
            "Batman",
            "some url",
            "7",
            "2001",
            "some description",
            "usa",
            "fighting",
            "some url"
        )
    }

    @AfterEach
    fun tearDown() {

        Mockito.reset(
            loadMovieItemUseCase,
            updateMovieItemUseCase
        )
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should interact correct useCases when onRefreshItem() and item is not exist in base`() =
        runTest {

            Mockito.`when`(loadMovieItemUseCase(NOT_EXISTING_MOVIE_ID)).thenReturn(null)

            viewModelMovieItem.onRefreshItem(NOT_EXISTING_MOVIE_ID)

            Mockito.verify(loadMovieItemUseCase).invoke(NOT_EXISTING_MOVIE_ID)
            Mockito.verify(updateMovieItemUseCase).invoke(NOT_EXISTING_MOVIE_ID)
            Mockito.verifyNoMoreInteractions(loadMovieItemUseCase, updateMovieItemUseCase)
        }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should interact correct useCases when onRefreshItem() and item is exist in base`() =
        runTest {

            Mockito.`when`(loadMovieItemUseCase(EXISTING_MOVIE_ID)).thenReturn(expectedMovieItem)
            Mockito.`when`(updateMovieItemUseCase(EXISTING_MOVIE_ID)).thenReturn(Unit)

            viewModelMovieItem.onRefreshItem(EXISTING_MOVIE_ID)

            Mockito.verify(loadMovieItemUseCase).invoke(EXISTING_MOVIE_ID)
            Mockito.verify(updateMovieItemUseCase).invoke(EXISTING_MOVIE_ID)
            Mockito.verifyNoMoreInteractions(loadMovieItemUseCase, updateMovieItemUseCase)
        }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct state when onRefreshItem() and network call is not Success and item no exist in base`() =
        runTest {

            val expectedErrorMessage = "Unable to load movie item"
            val error = RemoteLoadMovieItemError(expectedErrorMessage, Throwable())

            Mockito.`when`(updateMovieItemUseCase(NOT_EXISTING_MOVIE_ID)).thenAnswer { throw error }
            Mockito.`when`(loadMovieItemUseCase(NOT_EXISTING_MOVIE_ID)).thenReturn(null)

            viewModelMovieItem.movieItemState.captureValues {

                viewModelMovieItem.onRefreshItem(NOT_EXISTING_MOVIE_ID)

                Assertions.assertEquals(
                    values, listOf(
                        MovieItemState.UpdatingState,
                        MovieItemState.ErrorState(UPDATE_MOVIE_ITEM_ERROR_MESSAGE),
                        MovieItemState.NoItemState
                    )
                )
            }
        }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct state when onRefreshItem() and network call is not Success and item exist in base`() =
        runTest {

            val expectedErrorMessage = "Unable to load movie item"
            val error = RemoteLoadMovieItemError(expectedErrorMessage, Throwable())

            Mockito.`when`(updateMovieItemUseCase(EXISTING_MOVIE_ID)).thenAnswer { throw error }
            Mockito.`when`(loadMovieItemUseCase(EXISTING_MOVIE_ID)).thenReturn(expectedMovieItem)

            viewModelMovieItem.movieItemState.captureValues {

                viewModelMovieItem.onRefreshItem(EXISTING_MOVIE_ID)

                Assertions.assertEquals(
                    values, listOf(
                        MovieItemState.UpdatingState,
                        MovieItemState.ErrorState(UPDATE_MOVIE_ITEM_ERROR_MESSAGE),
                        MovieItemState.SuccessState
                    )
                )
            }
        }

    //
    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct item when onRefreshItem() and network call is not Success and item exist in base`() =
        runTest {

            val expectedErrorMessage = "Unable to load movie item"
            val error = RemoteLoadMovieItemError(expectedErrorMessage, Throwable())

            Mockito.`when`(updateMovieItemUseCase(EXISTING_MOVIE_ID)).thenAnswer { throw error }
            Mockito.`when`(loadMovieItemUseCase(EXISTING_MOVIE_ID)).thenReturn(expectedMovieItem)

            viewModelMovieItem.movieItem.captureValues {

                viewModelMovieItem.onRefreshItem(EXISTING_MOVIE_ID)

                Assertions.assertEquals(values, listOf(expectedMovieItem))
            }
        }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct state when onRefreshItem() and network call is success`() =
        runTest {

            Mockito.`when`(updateMovieItemUseCase(NOT_EXISTING_MOVIE_ID)).thenReturn(Unit)
            Mockito.`when`(loadMovieItemUseCase(NOT_EXISTING_MOVIE_ID))
                .thenReturn(expectedMovieItem)

            viewModelMovieItem.movieItemState.captureValues {

                viewModelMovieItem.onRefreshItem(NOT_EXISTING_MOVIE_ID)

                Assertions.assertEquals(
                    values, listOf(
                        MovieItemState.UpdatingState,
                        MovieItemState.SuccessState,
                        MovieItemState.SuccessState
                    )
                )
            }
        }


    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct item when onRefreshItem() and network call is success`() =
        runTest {

            Mockito.`when`(updateMovieItemUseCase(EXISTING_MOVIE_ID)).thenReturn(Unit)
            Mockito.`when`(loadMovieItemUseCase(EXISTING_MOVIE_ID)).thenReturn(expectedMovieItem)

            viewModelMovieItem.movieItem.captureValues {

                viewModelMovieItem.onRefreshItem(EXISTING_MOVIE_ID)

                Assertions.assertEquals(values, listOf(expectedMovieItem))
            }
        }

    companion object {
        private const val EXISTING_MOVIE_ID = 1
        private const val NOT_EXISTING_MOVIE_ID = 5
        private const val UPDATE_MOVIE_ITEM_ERROR_MESSAGE = "Unable to load movie item"
    }
}