package com.github.googelfist.moviesearcher.domain.mapper

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
                    ratingKinopoisk = it.rating.toFloat(),
                    year = it.year.toInt(),
                    country = it.countries.toString(),
                    genre = it.genres.toString()
                )
            )
        }
        return result
    }
}