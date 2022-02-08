package com.github.googelfist.moviesearcher.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.googelfist.moviesearcher.component
import com.github.googelfist.moviesearcher.databinding.FragmentItemMovieBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import javax.inject.Inject

class DetailFragment : Fragment() {

    @Inject
    lateinit var mainViewModelFabric: MainViewModelFabric

    private var _binding: FragmentItemMovieBinding? = null
    private val binding: FragmentItemMovieBinding
        get() = _binding!!

    private val mainViewModel by activityViewModels<MainViewModel> { mainViewModelFabric }

    override fun onAttach(context: Context) {
        requireActivity().component.inject(this)
        super.onAttach(context)
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

        mainViewModel.movieDetail.observe(viewLifecycleOwner) {
            Picasso.get().load(it.posterUrl).into(binding.ivPreviewMovieImage)
            binding.tvDetailCountry.text = it.country
            binding.tvDetailMovieName.text = it.nameOriginal
            binding.tvDetailGenre.text = it.genre
            binding.tvDetailYear.text = it.year
            binding.tvDetailDescription.text = it.description
            binding.includeDetailFragmentRating.tvDetailRating.text = it.ratingKinopoisk
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        fun getNewInstance(): DetailFragment {
            return DetailFragment()
        }
    }
}