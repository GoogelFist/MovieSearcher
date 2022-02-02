package com.github.googelfist.moviesearcher.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.googelfist.moviesearcher.R
import com.github.googelfist.moviesearcher.di.ApplicationComponent
import com.github.googelfist.moviesearcher.di.DaggerApplicationComponent
import com.google.android.material.button.MaterialButton
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    val component: ApplicationComponent by lazy { DaggerApplicationComponent.create() }

    @Inject
    lateinit var mainViewModelFabric: MainViewModelFabric

    private val mainViewModel by lazy { ViewModelProvider(this, mainViewModelFabric)[MainViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)

        findViewById<MaterialButton>(R.id.b_load).setOnClickListener { mainViewModel.onLoadTop250BestFilms(PAGE) }
    }

    companion object {
        private const val PAGE = 1
    }
}