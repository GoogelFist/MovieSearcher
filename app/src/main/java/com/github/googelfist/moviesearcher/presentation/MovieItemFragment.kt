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
import com.bumptech.glide.Glide
import com.github.googelfist.moviesearcher.R
import com.github.googelfist.moviesearcher.component
import com.github.googelfist.moviesearcher.databinding.FragmentItemMovieBinding
import com.github.googelfist.moviesearcher.presentation.states.MovieItemState
import com.github.googelfist.moviesearcher.presentation.viewmodel.ViewModelMovieItem
import com.github.googelfist.moviesearcher.presentation.viewmodel.ViewModelMovieItemFabric
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MovieItemFragment : Fragment() {

    @Inject
    lateinit var viewModelMovieItemFabric: ViewModelMovieItemFabric

    private var _binding: FragmentItemMovieBinding? = null
    private val binding: FragmentItemMovieBinding
        get() = _binding!!

    private val viewModelMovieItem by activityViewModels<ViewModelMovieItem> {
        viewModelMovieItemFabric
    }

    private var movieId = DEFAULT_MOVIE_ID

    override fun onAttach(context: Context) {
        context.component.inject(this)
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
        setOnSwipeListener()
        setupToolbar()
        observeViewModel(view)
        setupButtons(movieId)
        viewModelMovieItem.onRefreshItem(movieId)
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
        binding.scrollViewMovieItem.setOnTouchListener(object :
            OnSwipeTouchListener(requireActivity()) {
            override fun onSwipeRight() {
                requireActivity().supportFragmentManager.popBackStack()
            }
        })
    }

    private fun setupToolbar() {
        with(binding) {
            toolBarFragmentItemMovie.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            toolBarFragmentItemMovie.setNavigationOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun observeViewModel(view: View) {
        viewModelMovieItem.movieItemState.observe(viewLifecycleOwner) { state ->
            with(binding) {
                when (state) {
                    is MovieItemState.NoItemState -> {
                        progressBarFragmentItem.visibility = View.GONE
                        scrollViewMovieItem.visibility = View.INVISIBLE
                        toolBarFragmentItemMovie.visibility = View.INVISIBLE
                        txtNoItem.visibility = View.VISIBLE
                        refreshButtonMovieItem.visibility = View.VISIBLE
                    }
                    is MovieItemState.UpdatingState -> {
                        progressBarFragmentItem.visibility = View.VISIBLE
                        scrollViewMovieItem.visibility = View.INVISIBLE
                        toolBarFragmentItemMovie.visibility = View.INVISIBLE
                        txtNoItem.visibility = View.GONE
                        refreshButtonMovieItem.visibility = View.GONE
                    }
                    is MovieItemState.SuccessState -> {
                        progressBarFragmentItem.visibility = View.GONE
                        scrollViewMovieItem.visibility = View.VISIBLE
                        toolBarFragmentItemMovie.visibility = View.VISIBLE
                        txtNoItem.visibility = View.GONE
                        refreshButtonMovieItem.visibility = View.GONE
                    }
                    is MovieItemState.ErrorState -> {
                        Snackbar.make(view, state.message, Snackbar.LENGTH_SHORT).show()
                    }
                    else -> {
                        throw RuntimeException("Unknown state")
                    }
                }
            }
        }

        viewModelMovieItem.movieItem.observe(viewLifecycleOwner) {
            it?.let {
                with(binding.includeItemMovieInfo) {

                    Glide.with(requireActivity())
                        .load(it.posterUrl)
                        .placeholder(R.drawable.movie_list_item_background)
                        .centerCrop()
                        .into(itemMovieImage)

                    tvItemCountry.text = it.country
                    tvItemMovieName.text = it.nameOriginal
                    tvItemGenre.text = it.genre
                    tvItemYear.text = it.year
                    tvItemDescription.text = it.description
                    tvItemRating.text = it.ratingKinopoisk
                    setupWebIntent(it.webUrl)
                }
            }
        }
    }

    private fun setupWebIntent(url: String) {
        binding.imageButtonFragmentItemWebIntent.setOnClickListener {
            val webIntent = Intent(Intent.ACTION_VIEW)
            webIntent.data = Uri.parse(url)
            startActivity(webIntent)
        }
    }

    private fun setupButtons(movieId: Int) {
        binding.refreshButtonMovieItem.setOnClickListener {
            viewModelMovieItem.onRefreshItem(movieId)
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