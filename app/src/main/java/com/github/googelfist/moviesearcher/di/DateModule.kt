package com.github.googelfist.moviesearcher.di

import com.github.googelfist.moviesearcher.data.RepositoryImp
import com.github.googelfist.moviesearcher.data.datasourse.RemoteDataSource
import com.github.googelfist.moviesearcher.data.datasourse.network.RetrofitDataSourceImpl
import com.github.googelfist.moviesearcher.domain.Repository
import dagger.Binds
import dagger.Module

@Module
interface DateModule {

    @Binds
    fun bindRemoteDataSource(impl: RetrofitDataSourceImpl): RemoteDataSource

    @Binds
    fun bindRepository(impl: RepositoryImp): Repository
}