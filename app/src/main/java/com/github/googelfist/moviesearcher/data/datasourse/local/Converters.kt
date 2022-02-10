package com.github.googelfist.moviesearcher.data.datasourse.local

import androidx.room.TypeConverter
import com.github.googelfist.moviesearcher.data.datasourse.local.model.MoviePreviewDAO
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun listToJsonString(value: List<MoviePreviewDAO>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String) =
        Gson().fromJson(value, Array<MoviePreviewDAO>::class.java).toList()
}
