package com.github.googelfist.moviesearcher.data.datasourse.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieItemDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieListDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePageListDAO
import com.github.googelfist.moviesearcher.data.datasourse.local.model.PageCountDAO

@Dao
interface MovieDAO {
    @Query("SELECT * FROM movie_page_list_dao")
    fun loadAllMovieLists(): LiveData<List<MoviePageListDAO>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviePageList(moviesPageDAO: MoviePageListDAO)

    @Query("SELECT * FROM movie_item WHERE kinopoiskId = :id")
    suspend fun loadMovieItem(id: Int): MovieItemDAO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieItem(movieItemDAO: MovieItemDAO)

    @Query("SELECT pageCount FROM page_count")
    suspend fun loadPageCount(): PageCountDAO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPageCount(pageCount: PageCountDAO)
}