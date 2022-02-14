package com.github.googelfist.moviesearcher

import android.app.Application
import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.github.googelfist.moviesearcher.di.ApplicationComponent
import com.github.googelfist.moviesearcher.di.DaggerApplicationComponent
import com.github.googelfist.moviesearcher.data.RefreshMainDataWork
import java.util.concurrent.TimeUnit

class MainApplication : Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder().context(this).build()
        setupWorkerManagerJob(this)
    }
}

private fun setupWorkerManagerJob(context: Application) {
    val constraints = Constraints.Builder()
        .setRequiresCharging(true)
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .build()

    val work = PeriodicWorkRequestBuilder<RefreshMainDataWork>(1, TimeUnit.DAYS)
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context)
        .enqueueUniquePeriodicWork(
            RefreshMainDataWork::class.java.name,
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )
}

val Context.component: ApplicationComponent
    get() = when (this) {
        is MainApplication -> appComponent
        else -> this.applicationContext.component
    }