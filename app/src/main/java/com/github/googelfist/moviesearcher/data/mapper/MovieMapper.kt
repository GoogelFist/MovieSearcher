package com.github.googelfist.moviesearcher.data.mapper

import com.github.googelfist.moviesearcher.data.datasourse.network.model.Country
import com.github.googelfist.moviesearcher.data.datasourse.network.model.Genre
import com.github.googelfist.moviesearcher.data.datasourse.network.model.MovieDTO
import com.github.googelfist.moviesearcher.domain.model.MoviePreview
import javax.inject.Inject

class MovieMapper @Inject constructor(){

    fun mapMovieDTOtoMoviePreviewList(dto: MovieDTO): List<MoviePreview> {
        val films = dto.films
        val result = mutableListOf<MoviePreview>()
        films.forEach {
            result.add(
                MoviePreview(
                    kinopoiskId = it.filmId.toLong(),
                    nameRu = it.nameRu,
                    nameEn = it.nameEn,
                    posterUrlPreview = it.posterUrlPreview,
                    year = formatYear(it.year),
                    country = formatCountries(it.countries),
                    genre = formatGenres(it.genres)
                )
            )
        }
        return result
    }

    private fun formatCountries(countries: List<Country>): String {
        return countries.joinToString(separator = ", ", prefix = "Countries: ") {it.country}
    }

    private fun formatGenres(genres: List<Genre>): String {
        return genres.joinToString(separator = ", ", prefix = "Genres: ") {it.genre}
    }

    private fun formatYear(year: String): String {
        return "Year: $year"
    }
}