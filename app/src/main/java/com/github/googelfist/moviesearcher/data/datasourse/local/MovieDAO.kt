package com.github.googelfist.moviesearcher.data.datasourse.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieDetailDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePreviewDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePreviewListDAO

@Dao
interface MovieDAO {

    @Query("SELECT * FROM movie_preview_list_dao WHERE page = :page")
    suspend fun loadTop250BestFilms(page: Int): MoviePreviewListDAO

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviePreviewList(moviesPreviewDAO: MoviePreviewListDAO)

    @Query("SELECT * FROM movie_detail WHERE kinopoiskId = :id")
    suspend fun loadMovieDetail(id: Int): MovieDetailDAO

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetail(movieDetailDAO: MovieDetailDAO)
}