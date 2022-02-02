package com.github.googelfist.moviesearcher.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.googelfist.moviesearcher.R
import com.github.googelfist.moviesearcher.component

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, PreviewFragment.getNewInstance())
                .setReorderingAllowed(true)
                .commit()
        }
    }
}