package com.github.googelfist.moviesearcher.data

import com.github.googelfist.moviesearcher.data.datasourse.LocalDataSource
import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.data.datasourse.local.model.PageCountDAO
import com.github.googelfist.moviesearcher.data.datasourse.network.model.list.MovieListDTO
import com.github.googelfist.moviesearcher.data.mapper.MovieMapper
import com.github.googelfist.moviesearcher.domain.Repository
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val mapper: MovieMapper
) : Repository {

    private var pageNumber: Int = PAGE_COUNT_ONE
    private var top250PageCount: Int = PAGE_COUNT_ZERO

    private var previewMovies = mutableListOf<MovieList>()

    override suspend fun loadPageTop250BestFilms(): List<MovieList> {

        if (top250PageCount == PAGE_COUNT_ZERO) {
            top250PageCount = localLoadPageCount()
        }

        when {
            pageNumber == PAGE_COUNT_ONE -> {
                val localMovies = localLoadTop250BestFilms(PAGE_COUNT_ONE)
                previewMovies = if (localMovies.isEmpty()) {
                    val movies = remoteLoadTop250BestFilms(PAGE_COUNT_ONE)
                    localSaveTop250BestFilms(movies)

                    movies as MutableList<MovieList>
                } else {
                    localMovies as MutableList<MovieList>
                }
                increasePageNumber()
            }
            pageNumber < top250PageCount -> {
                val localMovies = localLoadTop250BestFilms(pageNumber)
                if (localMovies.isEmpty()) {
                    val movies = remoteLoadTop250BestFilms(pageNumber)
                    localSaveTop250BestFilms(movies)

                    previewMovies.addAll(movies)
                } else {
                    previewMovies.addAll(localMovies)
                }
                increasePageNumber()
            }
        }
        return previewMovies.toList()
    }

    override suspend fun loadMovieDetail(id: Int): MovieItem {
        return remoteLoadMovieDetail(id)
    }

    private suspend fun localLoadTop250BestFilms(page: Int): List<MovieList> {
        val result = localDataSource.loadMoviePageList(page)
        result?.let {
            return mapper.mapMoviePageListDAOtoMovieList(it)
        }
        return emptyList()
    }

    private suspend fun localSaveTop250BestFilms(movieList: List<MovieList>) {
        val moviePageListDAO = mapper.mapMovieListToMoviePageListDAO(pageNumber, movieList)

        localDataSource.insertMoviePageList(moviePageListDAO)
    }

    private suspend fun remoteLoadTop250BestFilms(page: Int): List<MovieList> {
        try {
            val result = remoteDataSource.loadTop250BestFilms(page)

            localSavePageCountDAO(result)

            return mapper.mapMovieListDTOtoMovieList(result)
        } catch (error: Throwable) {
            throw LoadTop250BestFilmsError("Unable to load top 250 best films", error)
        }
    }

    private suspend fun localLoadPageCount(): Int {
        val pageCountDAO = localDataSource.loadPageCount()
        pageCountDAO?.let {
            return pageCountDAO.pageCount
        }
        return PAGE_COUNT_ZERO
    }

    private suspend fun localSavePageCountDAO(result: MovieListDTO) {
        val pageCount = result.pagesCount
        localDataSource.insertPageCount(PageCountDAO(pageCount))
    }

    private suspend fun remoteLoadMovieDetail(id: Int): MovieItem {
        try {
            val result = remoteDataSource.loadMovieDetail(id)

            return mapper.mapMovieItemDTOtoMovieItem(result)
        } catch (error: Throwable) {
            throw LoadMovieDetailError("Unable to load movie detail", error)
        }
    }

    private fun increasePageNumber() {
        pageNumber++
    }

    companion object {
        private const val PAGE_COUNT_ZERO = 0
        private const val PAGE_COUNT_ONE = 1
    }
}

class LoadTop250BestFilmsError(message: String, cause: Throwable) : Throwable(message, cause)
class LoadMovieDetailError(message: String, cause: Throwable) : Throwable(message, cause)