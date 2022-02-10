package com.github.googelfist.moviesearcher.di

import com.github.googelfist.moviesearcher.data.RepositoryImp
import com.github.googelfist.moviesearcher.data.datasourse.LocalDataSource
import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.data.datasourse.local.RoomDataSourceImpl
import com.github.googelfist.moviesearcher.data.datasourse.network.RetrofitDataSourceImpl
import com.github.googelfist.moviesearcher.domain.Repository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DateModule {

    @Binds
    fun bindRemoteDataSource(impl: RetrofitDataSourceImpl): RemoteDataSource

    @Binds
    fun bindLocalDataSource(impl: RoomDataSourceImpl): LocalDataSource

    @Binds
    @Singleton
    fun bindRepository(impl: RepositoryImp): Repository
}