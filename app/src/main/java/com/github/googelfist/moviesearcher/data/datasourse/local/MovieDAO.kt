package com.github.googelfist.moviesearcher.data.datasourse.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieItemDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePageListDAO

@Dao
interface MovieDAO {

    @Query("SELECT * FROM movie_page_list_dao WHERE page = :page")
    suspend fun loadTop250BestFilms(page: Int): MoviePageListDAO

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviePreviewList(moviesPageDAO: MoviePageListDAO)

    @Query("SELECT * FROM movie_item WHERE kinopoiskId = :id")
    suspend fun loadMovieDetail(id: Int): MovieItemDAO

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetail(movieItemDAO: MovieItemDAO)
}