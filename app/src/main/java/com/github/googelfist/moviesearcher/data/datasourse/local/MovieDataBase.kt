package com.github.googelfist.moviesearcher.data.datasourse.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieDetailDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePreviewListDAO
import com.github.googelfist.moviesearcher.domain.model.MoviePreview

@Database(
    entities = [MovieDetailDAO::class, MoviePreview::class, MoviePreviewListDAO::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDataBase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDAO
}