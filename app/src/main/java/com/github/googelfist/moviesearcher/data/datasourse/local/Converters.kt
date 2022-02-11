package com.github.googelfist.moviesearcher.data.datasourse.local

import androidx.room.TypeConverter
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MovieListDAO
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun listToJsonString(value: List<MovieListDAO>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String) =
        Gson().fromJson(value, Array<MovieListDAO>::class.java).toList()
}
