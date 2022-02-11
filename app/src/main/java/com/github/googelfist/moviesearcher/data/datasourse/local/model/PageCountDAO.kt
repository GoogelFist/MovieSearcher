package com.github.googelfist.moviesearcher.data.datasourse.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "page_count")
data class PageCountDAO(
    @PrimaryKey
    val pageCount: Int
)