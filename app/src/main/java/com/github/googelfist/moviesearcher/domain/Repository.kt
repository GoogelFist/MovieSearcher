package com.github.googelfist.moviesearcher.domain

import androidx.lifecycle.LiveData
import com.github.googelfist.moviesearcher.data.model.MovieDTO

interface Repository {

    fun loadTop250BestFilms(): LiveData<List<MovieDTO>>

    fun loadTop100PopularFilms(): LiveData<List<MovieDTO>>
}