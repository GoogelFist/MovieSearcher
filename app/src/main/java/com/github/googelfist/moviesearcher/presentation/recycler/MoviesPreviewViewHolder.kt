package com.github.googelfist.moviesearcher.presentation.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.googelfist.moviesearcher.R
import com.github.googelfist.moviesearcher.domain.model.MoviePreview
import com.squareup.picasso.Picasso

class MoviesPreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imagePreview: ImageView = view.findViewById(R.id.iv_movie_preview)
    private val name: TextView = view.findViewById(R.id.tv_movie_name)
    private val genre: TextView = view.findViewById(R.id.tv_genres)
    private val country: TextView = view.findViewById(R.id.tv_countries)
    private val year: TextView = view.findViewById(R.id.tv_year)

    fun bind(movie: MoviePreview) {
        Picasso.get().load(movie.posterUrlPreview).into(this.imagePreview)
        name.text = movie.nameEn ?: movie.nameRu
        genre.text = movie.genre
        country.text = movie.country
        year.text = movie.year
    }
}