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
import com.github.googelfist.moviesearcher.data.datasourse.local.model.PageNumberDAO

@Dao
interface MovieDAO {
    @Query("SELECT * FROM movie_page_list_dao")
    fun loadAllMovieLists(): LiveData<List<MoviePageListDAO>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviePageList(moviesPageDAO: MoviePageListDAO)

    @Query("SELECT * FROM movie_item WHERE kinopoiskId = :id")
    fun loadMovieItem(id: Int): LiveData<MovieItemDAO?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieItem(movieItemDAO: MovieItemDAO)

    @Query("SELECT * FROM page_count WHERE id = :id")
    suspend fun loadPageCountById(id: Int): PageCountDAO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPageCount(pageCount: PageCountDAO)

    @Query("SELECT * FROM page_number WHERE id = :id")
    suspend fun loadPageNumberById(id: Int): PageNumberDAO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPageNumber(pageNumberDAO: PageNumberDAO)
}