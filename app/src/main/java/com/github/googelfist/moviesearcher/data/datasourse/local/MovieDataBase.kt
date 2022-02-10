package com.github.googelfist.moviesearcher.data.datasourse.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieDetailDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePreviewDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePreviewListDAO

@Database(
    entities = [MovieDetailDAO::class, MoviePreviewDAO::class, MoviePreviewListDAO::class],
    version = DB_VERSION,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class MovieDataBase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDAO
}

private const val DB_VERSION = 3