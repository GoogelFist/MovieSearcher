package com.github.googelfist.moviesearcher.data.mapper

import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieDetailDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePreviewDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePreviewListDAO
import com.github.googelfist.moviesearcher.data.datasourse.network.model.Country
import com.github.googelfist.moviesearcher.data.datasourse.network.model.Genre
import com.github.googelfist.moviesearcher.data.datasourse.network.model.detail.MovieDetailDTO
import com.github.googelfist.moviesearcher.data.datasourse.network.model.preview.MoviePreviewDTO
import com.github.googelfist.moviesearcher.domain.model.MovieDetail
import com.github.googelfist.moviesearcher.domain.model.MoviePreview
import javax.inject.Inject

class MovieMapper @Inject constructor() {

    fun mapMovieDTOtoMoviePreviewList(dto: MoviePreviewDTO): List<MoviePreview> {
        val films = dto.films
        val result = mutableListOf<MoviePreview>()
        films.forEach {
            result.add(
                MoviePreview(
                    kinopoiskId = it.filmId,
                    nameRu = it.nameRu,
                    nameEn = it.nameEn ?: it.nameRu,
                    posterUrl = it.posterUrl
                )
            )
        }
        return result
    }

    fun mapMoviePreviewToMoviePreviewListDAO(
        page: Int,
        moviePreview: List<MoviePreview>
    ): MoviePreviewListDAO {

        val moviePreviewListDAO = moviePreview.map {
            MoviePreviewDAO(
                kinopoiskId = it.kinopoiskId,
                nameRu = it.nameRu,
                nameEn = it.nameEn,
                posterUrl = it.posterUrl
            )
        }.toList()

        return MoviePreviewListDAO(page, moviePreviewListDAO)
    }

    fun mapMovieDTOtoMovieDetail(dto: MovieDetailDTO): MovieDetail {
        return MovieDetail(
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

    fun mapMovieDetailToMovieDAO(movieDetail: MovieDetail): MovieDetailDAO {
        return MovieDetailDAO(
            kinopoiskId = movieDetail.kinopoiskId,
            nameRu = movieDetail.nameRu,
            nameEn = movieDetail.nameEn,
            nameOriginal = movieDetail.nameOriginal,
            posterUrl = movieDetail.posterUrl,
            ratingKinopoisk = movieDetail.ratingKinopoisk,
            year = movieDetail.year,
            description = movieDetail.description,
            country = movieDetail.country,
            genre = movieDetail.genre
        )
    }

    private fun formatCountries(countries: List<Country>): String {
        return countries.joinToString(separator = ", ") { it.country }
    }

    private fun formatGenres(genres: List<Genre>): String {
        return genres.joinToString(separator = ", ") { it.genre }
    }
}