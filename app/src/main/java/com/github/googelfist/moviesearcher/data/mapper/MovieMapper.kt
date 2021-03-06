package com.github.googelfist.moviesearcher.data.mapper

import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieItemDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieListDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePageListDAO
import com.github.googelfist.moviesearcher.data.datasourse.network.model.Country
import com.github.googelfist.moviesearcher.data.datasourse.network.model.Genre
import com.github.googelfist.moviesearcher.data.datasourse.network.model.item.MovieItemDTO
import com.github.googelfist.moviesearcher.data.datasourse.network.model.list.MovieListDTO
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieMapper @Inject constructor(private val dispatcher: CoroutineDispatcher) {

    suspend fun mapMovieListDTOtoMovieList(dto: MovieListDTO): List<MovieList> {
        return withContext(dispatcher) {
            val films = dto.films
            val result = mutableListOf<MovieList>()
            films.forEach {
                result.add(
                    MovieList(
                        kinopoiskId = it.filmId,
                        nameRu = it.nameRu,
                        nameEn = it.nameEn ?: it.nameRu,
                        posterUrl = it.posterUrl
                    )
                )
            }
            result
        }
    }

    suspend fun mapListMoviePageListDAOtoMovieList(listMoviePageListDAO: List<MoviePageListDAO>?): List<MovieList> {
        return withContext(dispatcher) {
            listMoviePageListDAO?.let { it ->
                val result = mutableListOf<MovieList>()
                it.flatMap { moviePageList ->
                    moviePageList.moviesList.map { movieListDAO ->
                        result.add(
                            MovieList(
                                kinopoiskId = movieListDAO.kinopoiskId,
                                nameRu = movieListDAO.nameRu,
                                nameEn = movieListDAO.nameEn,
                                posterUrl = movieListDAO.posterUrl
                            )
                        )
                    }
                }
                return@withContext result
            }
            return@withContext emptyList()
        }
    }

    suspend fun mapMovieListToMoviePageListDAO(
        page: Int,
        movieList: List<MovieList>
    ): MoviePageListDAO {
        return withContext(dispatcher) {
            val moviePreviewListDAO = movieList.map {
                MovieListDAO(
                    kinopoiskId = it.kinopoiskId,
                    nameRu = it.nameRu,
                    nameEn = it.nameEn,
                    posterUrl = it.posterUrl
                )
            }.toList()

            MoviePageListDAO(page, moviePreviewListDAO)
        }
    }

    suspend fun mapMovieItemDTOtoMovieItem(dto: MovieItemDTO): MovieItem {
        return withContext(dispatcher) {
            MovieItem(
                kinopoiskId = dto.kinopoiskId,
                nameRu = dto.nameRu,
                nameEn = (dto.nameEn).let { dto.nameEn as String? } ?: dto.nameRu,
                nameOriginal = dto.nameOriginal ?: dto.nameRu,
                posterUrl = dto.posterUrl,
                ratingKinopoisk = dto.ratingKinopoisk.toString(),
                year = dto.year.toString(),
                description = dto.description,
                country = formatCountries(dto.countries),
                genre = formatGenres(dto.genres),
                webUrl = dto.webUrl
            )
        }
    }

    suspend fun mapMovieItemDAOToMovieItem(movieItem: MovieItemDAO): MovieItem {
        return withContext(dispatcher) {
            MovieItem(
                kinopoiskId = movieItem.kinopoiskId,
                nameRu = movieItem.nameRu,
                nameEn = movieItem.nameEn,
                nameOriginal = movieItem.nameOriginal,
                posterUrl = movieItem.posterUrl,
                ratingKinopoisk = movieItem.ratingKinopoisk,
                year = movieItem.year,
                description = movieItem.description,
                country = movieItem.country,
                genre = movieItem.genre,
                webUrl = movieItem.webUrl
            )
        }
    }

    suspend fun mapMovieItemToMovieItemDAO(movieItem: MovieItem): MovieItemDAO {
        return withContext(dispatcher) {
            MovieItemDAO(
                kinopoiskId = movieItem.kinopoiskId,
                nameRu = movieItem.nameRu,
                nameEn = movieItem.nameEn,
                nameOriginal = movieItem.nameOriginal,
                posterUrl = movieItem.posterUrl,
                ratingKinopoisk = movieItem.ratingKinopoisk,
                year = movieItem.year,
                description = movieItem.description,
                country = movieItem.country,
                genre = movieItem.genre,
                webUrl = movieItem.webUrl
            )
        }
    }

    private fun formatCountries(countries: List<Country>): String {
        return countries.joinToString(separator = ", ") { it.country }
    }

    private fun formatGenres(genres: List<Genre>): String {
        return genres.joinToString(separator = ", ") { it.genre }
    }
}