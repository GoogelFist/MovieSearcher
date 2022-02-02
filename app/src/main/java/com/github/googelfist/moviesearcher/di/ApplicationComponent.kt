package com.github.googelfist.moviesearcher.di

import com.github.googelfist.moviesearcher.presentation.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [DateModule::class])
@Singleton
interface ApplicationComponent {

    fun inject(activity: MainActivity)
}