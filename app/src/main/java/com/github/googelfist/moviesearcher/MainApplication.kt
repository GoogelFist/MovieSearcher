package com.github.googelfist.moviesearcher

import android.app.Application
import android.content.Context
import com.github.googelfist.moviesearcher.di.ApplicationComponent
import com.github.googelfist.moviesearcher.di.DaggerApplicationComponent

class MainApplication : Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder().context(this).build()
    }
}

val Context.component: ApplicationComponent
    get() = when (this) {
        is MainApplication -> appComponent
        else -> this.applicationContext.component
    }