package com.github.googelfist.moviesearcher.data.mapper

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
                    nameEn = it.nameEn  ?: it.nameRu,
                    posterUrl = it.posterUrl
                )
            )
        }
        return result
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

    private fun formatCountries(countries: List<Country>): String {
        return countries.joinToString(separator = ", ") { it.country }
    }

    private fun formatGenres(genres: List<Genre>): String {
        return genres.joinToString(separator = ", ") { it.genre }
    }
}