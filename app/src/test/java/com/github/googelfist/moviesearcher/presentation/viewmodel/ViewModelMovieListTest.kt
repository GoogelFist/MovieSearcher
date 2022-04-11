package com.github.googelfist.moviesearcher.presentation.viewmodel

import com.github.googelfist.moviesearcher.data.RemoteLoadMovieListError
import com.github.googelfist.moviesearcher.domain.LoadMovieListUseCase
import com.github.googelfist.moviesearcher.domain.UpdateMovieListUseCase
import com.github.googelfist.moviesearcher.domain.model.MovieList
import com.github.googelfist.moviesearcher.presentation.states.MovieListState
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
import org.mockito.kotlin.times

internal class ViewModelMovieListTest {

    private val loadMovieListUseCase = mock<LoadMovieListUseCase>()
    private val updateMovieListUseCase = mock<UpdateMovieListUseCase>()

    private lateinit var viewModelMovieList: ViewModelMovieList
    private lateinit var expectedMovieList: List<MovieList>

    @BeforeEach
    fun setUp() {

        expectedMovieList = listOf(
            MovieList(1, "Batman", "Batman", "some url")
        )
    }

    @AfterEach
    fun tearDown() {

        Mockito.reset(loadMovieListUseCase, updateMovieListUseCase)
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should interact correct useCases when view model init and list is not exist in base`() =
        runTest {

            Mockito.`when`(loadMovieListUseCase()).thenReturn(emptyList())
                .thenReturn(expectedMovieList)

            viewModelMovieList = ViewModelMovieList(loadMovieListUseCase, updateMovieListUseCase)

            Mockito.verify(loadMovieListUseCase, times(TWO_TIMES_VALUE)).invoke()
            Mockito.verify(updateMovieListUseCase).invoke()
            Mockito.verifyNoMoreInteractions(updateMovieListUseCase, loadMovieListUseCase)
        }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should interact correct useCases when view model init and list is exist in base`() =
        runTest {

            Mockito.`when`(loadMovieListUseCase()).thenReturn(expectedMovieList)
            Mockito.`when`(updateMovieListUseCase()).thenReturn(Unit)

            viewModelMovieList = ViewModelMovieList(loadMovieListUseCase, updateMovieListUseCase)

            Mockito.verify(loadMovieListUseCase, times(TWO_TIMES_VALUE)).invoke()
            Mockito.verifyNoMoreInteractions(updateMovieListUseCase, loadMovieListUseCase)
        }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct state when onRefreshList() and network call is not Success and list no exist in base`() =
        runTest {

            val expectedErrorMessage = "Unable to load movie list"
            val error = RemoteLoadMovieListError(expectedErrorMessage, Throwable())

            Mockito.`when`(updateMovieListUseCase()).thenAnswer { throw error }
            Mockito.`when`(loadMovieListUseCase()).thenReturn(emptyList())

            viewModelMovieList = ViewModelMovieList(loadMovieListUseCase, updateMovieListUseCase)

            viewModelMovieList.movieListState.captureValues {
                Assertions.assertEquals(values, listOf(MovieListState.NoListState))

                viewModelMovieList.onRefreshList()

                Assertions.assertEquals(
                    values, listOf(
                        MovieListState.NoListState,
                        MovieListState.UpdatingState,
                        MovieListState.ErrorState(UPDATE_MOVIE_LIST_ERROR_MESSAGE),
                        MovieListState.NoListState
                    )
                )
            }
        }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct state when onRefreshList() and network call is not Success and list exist in base`() =
        runTest {

            val expectedErrorMessage = "Unable to load movie list"
            val error = RemoteLoadMovieListError(expectedErrorMessage, Throwable())

            Mockito.`when`(updateMovieListUseCase()).thenAnswer { throw error }
            Mockito.`when`(loadMovieListUseCase()).thenReturn(expectedMovieList)

            viewModelMovieList = ViewModelMovieList(loadMovieListUseCase, updateMovieListUseCase)

            viewModelMovieList.movieListState.captureValues {
                Assertions.assertEquals(values, listOf(MovieListState.SuccessState))

                viewModelMovieList.onRefreshList()

                Assertions.assertEquals(
                    values, listOf(
                        MovieListState.SuccessState,
                        MovieListState.UpdatingState,
                        MovieListState.ErrorState(UPDATE_MOVIE_LIST_ERROR_MESSAGE),
                        MovieListState.SuccessState
                    )
                )
            }
        }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct movie list when onRefreshList() and network call is not Success and list exist in base`() =
        runTest {

            val expectedErrorMessage = "Unable to load movie list"
            val error = RemoteLoadMovieListError(expectedErrorMessage, Throwable())

            Mockito.`when`(updateMovieListUseCase()).thenAnswer { throw error }
            Mockito.`when`(loadMovieListUseCase()).thenReturn(expectedMovieList)

            viewModelMovieList = ViewModelMovieList(loadMovieListUseCase, updateMovieListUseCase)

            viewModelMovieList.movieList.captureValues {
                Assertions.assertEquals(values, listOf(expectedMovieList))

                viewModelMovieList.onRefreshList()

                Assertions.assertEquals(values, listOf(expectedMovieList, expectedMovieList))
            }
        }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct state when onRefreshList() and network call is success and list no exist in base`() =
        runTest {

            Mockito.`when`(updateMovieListUseCase()).thenAnswer { Unit }
            Mockito.`when`(loadMovieListUseCase()).thenReturn(emptyList())
                .thenReturn(expectedMovieList)

            viewModelMovieList = ViewModelMovieList(loadMovieListUseCase, updateMovieListUseCase)

            viewModelMovieList.movieListState.captureValues {
                Assertions.assertEquals(values, listOf(MovieListState.SuccessState))

                viewModelMovieList.onRefreshList()

                Assertions.assertEquals(
                    values, listOf(
                        MovieListState.SuccessState,
                        MovieListState.UpdatingState,
                        MovieListState.SuccessState,
                        MovieListState.SuccessState
                    )
                )
            }
        }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct state when onRefreshList() and network call is success and list exist in base`() =
        runTest {

            Mockito.`when`(updateMovieListUseCase()).thenAnswer { Unit }
            Mockito.`when`(loadMovieListUseCase()).thenReturn(expectedMovieList)

            viewModelMovieList = ViewModelMovieList(loadMovieListUseCase, updateMovieListUseCase)

            viewModelMovieList.movieListState.captureValues {
                Assertions.assertEquals(values, listOf(MovieListState.SuccessState))

                viewModelMovieList.onRefreshList()

                Assertions.assertEquals(
                    values, listOf(
                        MovieListState.SuccessState,
                        MovieListState.UpdatingState,
                        MovieListState.SuccessState,
                        MovieListState.SuccessState
                    )
                )
            }
        }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct movie list when onRefreshList() and network call is success and list exist in base`() =
        runTest {

            Mockito.`when`(updateMovieListUseCase()).thenAnswer { Unit }
            Mockito.`when`(loadMovieListUseCase()).thenReturn(expectedMovieList)

            viewModelMovieList = ViewModelMovieList(loadMovieListUseCase, updateMovieListUseCase)

            viewModelMovieList.movieList.captureValues {
                Assertions.assertEquals(values, listOf(expectedMovieList))

                viewModelMovieList.onRefreshList()

                Assertions.assertEquals(values, listOf(expectedMovieList, expectedMovieList))
            }
        }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct movie list when onRefreshList() and network call is success and list not exist in base`() =
        runTest {

            Mockito.`when`(updateMovieListUseCase()).thenAnswer { Unit }
            Mockito.`when`(loadMovieListUseCase()).thenReturn(emptyList())
                .thenReturn(expectedMovieList)

            viewModelMovieList = ViewModelMovieList(loadMovieListUseCase, updateMovieListUseCase)

            viewModelMovieList.movieList.captureValues {
                Assertions.assertEquals(values, listOf(expectedMovieList))

                viewModelMovieList.onRefreshList()

                Assertions.assertEquals(values, listOf(expectedMovieList, expectedMovieList))
            }
        }


    companion object {
        private const val TWO_TIMES_VALUE = 2
        private const val UPDATE_MOVIE_LIST_ERROR_MESSAGE = "Unable to load movie list"
    }
}
