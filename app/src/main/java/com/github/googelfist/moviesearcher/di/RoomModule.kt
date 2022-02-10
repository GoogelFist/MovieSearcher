package com.github.googelfist.moviesearcher.di

import android.app.Application
import androidx.room.Room
import com.github.googelfist.moviesearcher.data.datasourse.local.MovieDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideDataRoomDatabase(application: Application): MovieDataBase {
        return Room.databaseBuilder(application, MovieDataBase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesMovieDAO(movieDataBase: MovieDataBase) = movieDataBase.getMovieDao()

    companion object {
        private const val DB_NAME = "movie_database"
    }
}