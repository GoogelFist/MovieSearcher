package com.github.googelfist.moviesearcher.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.googelfist.moviesearcher.component
import com.github.googelfist.moviesearcher.databinding.FragmentItemMovieBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import javax.inject.Inject

class DetailFragment : Fragment() {

    @Inject
    lateinit var mainViewModelFabric: MainViewModelFabric

    private var movieId = DEFAULT_MOVIE_ID

    private var _binding: FragmentItemMovieBinding? = null
    private val binding: FragmentItemMovieBinding
        get() = _binding!!

    private val mainViewModel by lazy {
        ViewModelProvider(
            this,
            mainViewModelFabric
        )[MainViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        requireActivity().component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        parseParams()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show() }
        }
        mainViewModel.onLoadMovieDetail(movieId)

        mainViewModel.movieDetail.observe(viewLifecycleOwner) {
            Picasso.get().load(it.posterUrl).into(binding.ivPreviewMovieImage)
            binding.tvPreviewCountry.text = it.country
            binding.tvDetailMovieName.text = it.nameOriginal
            binding.tvPreviewGenre.text = it.genre
            binding.tvYear.text = it.year
            binding.tvRating.text = it.ratingKinopoisk
            binding.tvDetailDescription.text = it.description
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun parseParams() {
        val args = requireArguments()
        if (args.containsKey(MOVIE_ID)) {
            movieId = args.getInt(MOVIE_ID)
        }
    }

    companion object {
        private const val MOVIE_ID = "movie id"
        private const val DEFAULT_MOVIE_ID = 301

        fun getNewInstance(): DetailFragment {
            return DetailFragment()
        }

        fun getNewInstanceWithId(id: Int): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(MOVIE_ID, id)
                }
            }
        }
    }
}