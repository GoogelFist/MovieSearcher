package com.github.googelfist.moviesearcher.di

import com.github.googelfist.moviesearcher.presentation.DetailFragment
import com.github.googelfist.moviesearcher.presentation.MainActivity
import com.github.googelfist.moviesearcher.presentation.PreviewFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [DateModule::class, RetrofitModule::class])
@Singleton
interface ApplicationComponent {

    fun inject(previewFragment: PreviewFragment)

    fun inject(detailFragment: DetailFragment)
}