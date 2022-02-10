package com.github.googelfist.moviesearcher.data.mapper

import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieDetailDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePreviewDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePreviewListDAO
import com.github.googelfist.moviesearcher.data.datasourse.network.model.Country
import com.github.googelfist.moviesearcher.data.datasourse.network.model.Genre
import com.github.googelfist.moviesearcher.data.datasourse.network.model.detail.MovieDetailDTO
import com.github.googelfist.moviesearcher.data.datasourse.network.model.preview.MoviePreviewDTO
import com.github.googelfist.moviesearcher.domain.model.MovieItem
import com.github.googelfist.moviesearcher.domain.model.MovieList
import javax.inject.Inject

class MovieMapper @Inject constructor() {

    fun mapMovieDTOtoMoviePreviewList(dto: MoviePreviewDTO): List<MovieList> {
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
        return result
    }

    fun mapMovieListDAOtoMoviePreviewList(moviePreviewListDAO: MoviePreviewListDAO): List<MovieList> {
        val films = moviePreviewListDAO.moviesPreview
        val result = mutableListOf<MovieList>()
        films.forEach {
            result.add(
                MovieList(
                    kinopoiskId = it.kinopoiskId,
                    nameRu = it.nameRu,
                    nameEn = it.nameEn,
                    posterUrl = it.posterUrl
                )
            )
        }
        return result
    }

    fun mapMoviePreviewToMoviePreviewListDAO(
        page: Int,
        movieList: List<MovieList>
    ): MoviePreviewListDAO {

        val moviePreviewListDAO = movieList.map {
            MoviePreviewDAO(
                kinopoiskId = it.kinopoiskId,
                nameRu = it.nameRu,
                nameEn = it.nameEn,
                posterUrl = it.posterUrl
            )
        }.toList()

        return MoviePreviewListDAO(page, moviePreviewListDAO)
    }

    fun mapMovieDTOtoMovieDetail(dto: MovieDetailDTO): MovieItem {
        return MovieItem(
            kinopoiskId = dto.kinopoiskId,
            nameRu = dto.nameRu,
            nameEn = (dto.nameEn).let { dto.nameEn as String? } ?: dto.nameRu,
            nameOriginal = dto.nameOriginal ?: dto.nameRu,
            posterUrl = dto.posterUrl,
            ratingKinopoisk = dto.ratingKinopoisk.toString(),
            year = dto.year.toString(),
            description = dto.description,
            country = formatCountries(dto.countries),
            genre = formatGenres(dto.genres)
        )
    }

    fun mapMovieDetailToMovieDAO(movieItem: MovieItem): MovieDetailDAO {
        return MovieDetailDAO(
            kinopoiskId = movieItem.kinopoiskId,
            nameRu = movieItem.nameRu,
            nameEn = movieItem.nameEn,
            nameOriginal = movieItem.nameOriginal,
            posterUrl = movieItem.posterUrl,
            ratingKinopoisk = movieItem.ratingKinopoisk,
            year = movieItem.year,
            description = movieItem.description,
            country = movieItem.country,
            genre = movieItem.genre
        )
    }

    private fun formatCountries(countries: List<Country>): String {
        return countries.joinToString(separator = ", ") { it.country }
    }

    private fun formatGenres(genres: List<Genre>): String {
        return genres.joinToString(separator = ", ") { it.genre }
    }
}