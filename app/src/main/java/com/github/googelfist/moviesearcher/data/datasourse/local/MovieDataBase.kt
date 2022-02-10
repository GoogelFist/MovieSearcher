package com.github.googelfist.moviesearcher.data.datasourse.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieItemDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieListDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePageListDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.PageCountDAO

@Database(
    entities = [MovieItemDAO::class, MovieListDAO::class, MoviePageListDAO::class, PageCountDAO::class],
    version = DB_VERSION,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class MovieDataBase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDAO
}

private const val DB_VERSION = 5