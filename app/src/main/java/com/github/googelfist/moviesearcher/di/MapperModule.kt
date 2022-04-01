package com.github.googelfist.moviesearcher.di

import com.github.googelfist.moviesearcher.data.mapper.MovieMapper
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

@Module
class MapperModule {

    @Provides
    fun provideMovieMapper(): MovieMapper {
        return MovieMapper(Dispatchers.Default)
    }
}