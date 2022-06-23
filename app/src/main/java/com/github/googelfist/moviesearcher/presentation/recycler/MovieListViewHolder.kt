package com.github.googelfist.moviesearcher.presentation.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.googelfist.moviesearcher.R
import com.github.googelfist.moviesearcher.domain.model.MovieList

class MovieListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imagePreview: ImageView = view.findViewById(R.id.iv_item_movie_image)
    private val name: TextView = view.findViewById(R.id.tv_list_movie_name)
    private val number: TextView = view.findViewById(R.id.tv_list_number)

    fun bind(movie: MovieList, position: Int) {

        Glide.with(imagePreview.context)
            .load(movie.posterUrl)
            .centerCrop()
            .placeholder(R.drawable.movie_list_item_background)
            .into(this.imagePreview)

        name.text = movie.nameEn

        val numberOfPosition = position + ONE_VALUE
        number.text = "$numberOfPosition"
    }

    companion object {
        private const val ONE_VALUE = 1
    }
}