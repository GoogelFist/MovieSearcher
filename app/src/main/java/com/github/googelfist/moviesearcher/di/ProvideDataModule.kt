package com.github.googelfist.moviesearcher.di

import com.github.googelfist.moviesearcher.data.RepositoryImp
import com.github.googelfist.moviesearcher.data.datasourse.LocalDataSource
import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.data.mapper.MovieMapper
import com.github.googelfist.moviesearcher.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class ProvideDataModule {

    @Provides
    fun provideMovieMapper(): MovieMapper {
        return MovieMapper(Dispatchers.Default)
    }

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource): Repository {
        return RepositoryImp(remoteDataSource, localDataSource, dispatcher = Dispatchers.IO)
    }
}