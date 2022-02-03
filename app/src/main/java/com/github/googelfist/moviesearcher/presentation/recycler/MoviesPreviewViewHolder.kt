package com.github.googelfist.moviesearcher.presentation.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.googelfist.moviesearcher.R

class MoviesPreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imagePreview: ImageView = view.findViewById(R.id.iv_movie_preview)
    val name: TextView = view.findViewById(R.id.tv_movie_name)
    val genre: TextView = view.findViewById(R.id.tv_genres)
    val country: TextView = view.findViewById(R.id.tv_countries)
    val year: TextView = view.findViewById(R.id.tv_year)
}