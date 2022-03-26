package com.github.googelfist.moviesearcher.data.datasourse.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "page_number")
data class PageNumberDAO(
    @PrimaryKey
    val id: Int,
    val pageNumber: Int
)
