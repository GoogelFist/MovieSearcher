package com.github.googelfist.moviesearcher.di

import android.app.Application
import com.github.googelfist.moviesearcher.presentation.DetailFragment
import com.github.googelfist.moviesearcher.presentation.MainActivity
import com.github.googelfist.moviesearcher.presentation.PreviewFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [DateModule::class, RetrofitModule::class, RoomModule::class])
@Singleton
interface ApplicationComponent {

    fun inject(previewFragment: PreviewFragment)

    fun inject(detailFragment: DetailFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(application: Application): Builder

        fun build(): ApplicationComponent
    }
}