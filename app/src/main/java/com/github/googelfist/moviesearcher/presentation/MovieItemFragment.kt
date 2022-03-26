package com.github.googelfist.moviesearcher.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.googelfist.moviesearcher.R
import com.github.googelfist.moviesearcher.component
import com.github.googelfist.moviesearcher.databinding.FragmentItemMovieBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import javax.inject.Inject

class MovieItemFragment : Fragment() {

    @Inject
    lateinit var mainViewModelFabric: MainViewModelFabric

    private var _binding: FragmentItemMovieBinding? = null
    private val binding: FragmentItemMovieBinding
        get() = _binding!!

    private val mainViewModel by activityViewModels<MainViewModel> { mainViewModelFabric }

    private var movieId = DEFAULT_MOVIE_ID

    override fun onAttach(context: Context) {
        requireActivity().component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
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
//        mainViewModel.onLoadMovieItem(movieId)
        setOnSwipeListener()
        setupToolbar()
        observeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(KEY_ID)) {
            throw IllegalArgumentException("Movie id is absent")
        }
        movieId = args.getInt(KEY_ID)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnSwipeListener() {
        binding.svMovieItem.setOnTouchListener(object : OnSwipeTouchListener(requireActivity()) {
            override fun onSwipeRight() {
                requireActivity().supportFragmentManager.popBackStack()
            }
        })
    }

    private fun setupToolbar() {
        binding.tbFragmentItemMovie.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.tbFragmentItemMovie.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun observeViewModel() {
        mainViewModel.snackBar.observe(viewLifecycleOwner) {
            it?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            mainViewModel.onSnackBarShown()
            }
        }

        mainViewModel.movieItem.observe(viewLifecycleOwner) {
            it?.let {
                Picasso.get().load(it.posterUrl).into(binding.ivItemMovieImage)
                binding.tvItemCountry.text = it.country
                binding.tvItemMovieName.text = it.nameOriginal
                binding.tvItemGenre.text = it.genre
                binding.tvItemYear.text = it.year
                binding.tvItemDescription.text = it.description
                binding.includeItemFragmentRating.tvItemRating.text = it.ratingKinopoisk
                setupWebIntent(it.webUrl)
            }
        }
    }

    private fun setupWebIntent(url: String) {
        binding.ibFragmentItemWebIntent.setOnClickListener {
            val webIntent = Intent(Intent.ACTION_VIEW)
            webIntent.data = Uri.parse(url)
            startActivity(webIntent)
        }
    }

    companion object {
        private const val KEY_ID = "key id"
        private const val DEFAULT_MOVIE_ID = 0

        fun getNewInstance(id: Int): MovieItemFragment {
            return MovieItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_ID, id)
                }
            }
        }
    }
}