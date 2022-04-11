package com.github.googelfist.moviesearcher.di

import com.github.googelfist.moviesearcher.data.datasourse.LocalDataSource
import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.data.datasourse.local.RoomDataSourceImpl
import com.github.googelfist.moviesearcher.data.datasourse.network.RetrofitDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
interface BindDateModule {

    @Binds
    fun bindRemoteDataSource(impl: RetrofitDataSourceImpl): RemoteDataSource

    @Binds
    fun bindLocalDataSource(impl: RoomDataSourceImpl): LocalDataSource
}