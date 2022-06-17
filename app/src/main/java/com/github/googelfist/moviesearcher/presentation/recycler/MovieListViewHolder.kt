package com.github.googelfist.moviesearcher.presentation.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.googelfist.moviesearcher.R
import com.github.googelfist.moviesearcher.domain.model.MovieList
import com.squareup.picasso.Picasso

class MovieListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imagePreview: ImageView = view.findViewById(R.id.iv_item_movie_image)
    private val name: TextView = view.findViewById(R.id.tv_list_movie_name)
    private val number: TextView = view.findViewById(R.id.tv_list_number)

    fun bind(movie: MovieList, position: Int) {

        Picasso
            .get()
            .load(movie.posterUrl)
            .resize(350, 500)
            .centerCrop()
            .into(this.imagePreview)

        name.text = movie.nameEn

        val numberOfPosition = position + ONE_VALUE
        number.text = "$numberOfPosition"
    }

    companion object {
        private const val ONE_VALUE = 1
    }
}