package com.github.googelfist.moviesearcher.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.googelfist.moviesearcher.data.RemoteLoadMovieItemError
import com.github.googelfist.moviesearcher.data.RemoteLoadMovieListError
import com.github.googelfist.moviesearcher.data.RemoteLoadPageCountError
import com.github.googelfist.moviesearcher.domain.LoadMovieItemUseCase
import com.github.googelfist.moviesearcher.domain.LoadMovieListUseCase
import com.github.googelfist.moviesearcher.domain.UpdateMovieItemUseCase
import com.github.googelfist.moviesearcher.domain.UpdateMovieListUseCase
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.kotlin.mock

internal class MainViewModelTest {

    private val loadMovieListUseCase = mock<LoadMovieListUseCase>()
    private val loadMovieItemUseCase = mock<LoadMovieItemUseCase>()
    private val updateMovieListUseCase = mock<UpdateMovieListUseCase>()
    private val updateMovieItemUseCase = mock<UpdateMovieItemUseCase>()

    private lateinit var viewModel: MainViewModel
    private var expectedMovieItem: MovieItem? = null
    private lateinit var expectedMovieList: List<MovieList>


    @BeforeEach
    fun setUp() {

        viewModel = MainViewModel(
            loadMovieListUseCase,
            loadMovieItemUseCase,
            updateMovieListUseCase,
            updateMovieItemUseCase
        )

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

        expectedMovieList = listOf(
            MovieList(
                1,
                "Batman",
                "Batman",
                "some url"
            )
        )
    }

    @AfterEach
    fun tearDown() {

        Mockito.reset(
            loadMovieListUseCase,
            loadMovieItemUseCase,
            updateMovieListUseCase,
            updateMovieItemUseCase
        )
    }

    @Test
    fun `should return correct data when movie list is exist in data base`() {
        val expectedMovieListLD: LiveData<List<MovieList>> = MutableLiveData(expectedMovieList)

        Mockito.`when`(loadMovieListUseCase()).thenReturn(expectedMovieListLD)

        viewModel.onLoadMovieList()
        val actualMovieListLD = viewModel.movieList

        Mockito.verify(loadMovieListUseCase).invoke()
        Assertions.assertEquals(expectedMovieListLD, actualMovieListLD)
    }

    @Test
    fun `should return correct data when movie list is not exist in data base`() {
        expectedMovieList = emptyList()
        val expectedMovieListLD: LiveData<List<MovieList>> = MutableLiveData(expectedMovieList)

        Mockito.`when`(loadMovieListUseCase()).thenReturn(expectedMovieListLD)

        viewModel.onLoadMovieList()
        val actualMovieListLD = viewModel.movieList

        Mockito.verify(loadMovieListUseCase).invoke()
        Assertions.assertEquals(expectedMovieListLD, actualMovieListLD)
    }

    @Test
    fun `should return correct data when movie item is exist in data base`() {
        val expectedMovieItemLD: LiveData<MovieItem> = MutableLiveData(expectedMovieItem)

        Mockito.`when`(loadMovieItemUseCase(EXISTING_MOVIE_ID)).thenReturn(expectedMovieItemLD)

        viewModel.onLoadMovieItem(EXISTING_MOVIE_ID)
        val actualMovieItemLD = viewModel.movieItem

        Mockito.verify(loadMovieItemUseCase).invoke(EXISTING_MOVIE_ID)
        Assertions.assertEquals(expectedMovieItemLD, actualMovieItemLD)
    }

    @Test
    fun `should return correct data when movie item is not exist in data base`() {
        expectedMovieItem = null
        val expectedMovieItemLD: LiveData<MovieItem> = MutableLiveData(expectedMovieItem)

        Mockito.`when`(loadMovieItemUseCase(NOT_EXISTING_MOVIE_ID)).thenReturn(expectedMovieItemLD)

        viewModel.onLoadMovieItem(NOT_EXISTING_MOVIE_ID)
        val actualMovieItemLD = viewModel.movieItem

        Mockito.verify(loadMovieItemUseCase).invoke(NOT_EXISTING_MOVIE_ID)
        Assertions.assertEquals(expectedMovieItemLD, actualMovieItemLD)
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should call UpdateMovieLIstUseCase when call onUpdateMovieList`() = runTest {

        viewModel.onUpdateMovieList()

        Mockito.verify(updateMovieListUseCase).invoke()
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should handle RemoteLoadMovieListError when call onUpdateMovieList`() = runTest {

        val expectedErrorMessage = "Unable to load movie list"
        val error = RemoteLoadMovieListError(expectedErrorMessage, Throwable())
        Mockito.`when`(updateMovieListUseCase.invoke()).thenAnswer { throw error }

        viewModel.onUpdateMovieList()

        val actualErrorMessage = viewModel.snackBar.getValueForTest()

        Mockito.verify(updateMovieListUseCase).invoke()
        Assertions.assertEquals(expectedErrorMessage, actualErrorMessage)
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should handle RemoteLoadPageCountError when call onUpdateMovieList`() = runTest {

        val expectedErrorMessage = "Unable to load page count"
        val error = RemoteLoadPageCountError(expectedErrorMessage, Throwable())
        Mockito.`when`(updateMovieListUseCase.invoke()).thenAnswer { throw error }

        viewModel.onUpdateMovieList()

        val actualErrorMessage = viewModel.snackBar.getValueForTest()

        Mockito.verify(updateMovieListUseCase).invoke()
        Assertions.assertEquals(expectedErrorMessage, actualErrorMessage)
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct loading value when call onUpdateMovieList without error`() {

        viewModel.loading.captureValues {
            Assertions.assertEquals(values, listOf(false))
            viewModel.onUpdateMovieList()
            Assertions.assertEquals(values, listOf(false, true, false))
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct loading value when call onUpdateMovieList with error`() = runTest {

        val errorMessage = "Unable to load movie list"
        val error = RemoteLoadMovieListError(errorMessage, Throwable())
        Mockito.`when`(updateMovieListUseCase.invoke()).thenAnswer { throw error }

        viewModel.loading.captureValues {
            Assertions.assertEquals(values, listOf(false))
            viewModel.onUpdateMovieList()
            Assertions.assertEquals(values, listOf(false, true, false))
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct snackBar value when call onSnackBarShown`() = runTest {

        val errorMessage = "Unable to load movie list"
        val error = RemoteLoadMovieListError(errorMessage, Throwable())
        Mockito.`when`(updateMovieListUseCase.invoke()).thenAnswer { throw error }

        viewModel.snackBar.captureValues {
            viewModel.onUpdateMovieList()
            Assertions.assertEquals(values, listOf(errorMessage))
            viewModel.onSnackBarShown()
            Assertions.assertEquals(values, listOf(errorMessage, null))
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should call UpdateMovieItemUseCase when call onUpdateMovieList`() = runTest {

        viewModel.onUpdateMovieItem(EXISTING_MOVIE_ID)

        Mockito.verify(updateMovieItemUseCase).invoke(EXISTING_MOVIE_ID)
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should handle RemoteLoadMovieItemError when call onUpdateMovieItem`() = runTest {

        val expectedErrorMessage = "Unable to load movie item"
        val error = RemoteLoadMovieItemError(expectedErrorMessage, Throwable())
        Mockito.`when`(updateMovieItemUseCase.invoke(NOT_EXISTING_MOVIE_ID)).thenAnswer { throw error }

        viewModel.onUpdateMovieItem(NOT_EXISTING_MOVIE_ID)

        val actualErrorMessage = viewModel.snackBar.getValueForTest()

        Mockito.verify(updateMovieItemUseCase).invoke(NOT_EXISTING_MOVIE_ID)
        Assertions.assertEquals(expectedErrorMessage, actualErrorMessage)
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct loading value when call onUpdateMovieItem without error`() {

        viewModel.loading.captureValues {
            Assertions.assertEquals(values, listOf(false))
            viewModel.onUpdateMovieItem(EXISTING_MOVIE_ID)
            Assertions.assertEquals(values, listOf(false, true, false))
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
    fun `should set correct loading value when call onUpdateMovieItem with error`() = runTest {

        val errorMessage = "Unable to load movie item"
        val error = RemoteLoadMovieItemError(errorMessage, Throwable())
        Mockito.`when`(updateMovieItemUseCase.invoke(NOT_EXISTING_MOVIE_ID)).thenAnswer { throw error }

        viewModel.loading.captureValues {
            Assertions.assertEquals(values, listOf(false))
            viewModel.onUpdateMovieItem(NOT_EXISTING_MOVIE_ID)
            Assertions.assertEquals(values, listOf(false, true, false))
        }
    }

    companion object {
        private const val EXISTING_MOVIE_ID = 1
        private const val NOT_EXISTING_MOVIE_ID = 5
    }
}
