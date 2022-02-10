package com.github.googelfist.moviesearcher.di

import android.app.Application
import com.github.googelfist.moviesearcher.presentation.MovieItemFragment
import com.github.googelfist.moviesearcher.presentation.MovieListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [DateModule::class, RetrofitModule::class, RoomModule::class])
@Singleton
interface ApplicationComponent {

    fun inject(movieListFragment: MovieListFragment)

    fun inject(movieItemFragment: MovieItemFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(application: Application): Builder

        fun build(): ApplicationComponent
    }
}