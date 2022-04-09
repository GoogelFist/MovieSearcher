package com.github.googelfist.moviesearcher.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.googelfist.moviesearcher.R
import com.github.googelfist.moviesearcher.component
import com.github.googelfist.moviesearcher.databinding.FragmentListBinding
import com.github.googelfist.moviesearcher.presentation.recycler.MovieListAdapter
import com.github.googelfist.moviesearcher.presentation.states.MovieListState
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MovieListFragment : Fragment() {

    @Inject
    lateinit var mainViewModelFabric: MainViewModelFabric

    lateinit var movieListAdapter: MovieListAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding
        get() = _binding!!

    private val mainViewModel by activityViewModels<MainViewModel> { mainViewModelFabric }

    override fun onAttach(context: Context) {
        context.component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel(view)
        setupSwipeRefreshLayout(view)
        setMoviePreviewOnClickListener()
        setupButtons()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupRecyclerView() {
        val rvMoviesPreview = binding.recyclerViewMoviesList

        linearLayoutManager = LinearLayoutManager(requireActivity())
        rvMoviesPreview.layoutManager = linearLayoutManager

        movieListAdapter = MovieListAdapter()
        rvMoviesPreview.adapter = movieListAdapter

        movieListAdapter.onScrolledToBottomListener = {
            mainViewModel.onRefreshList()
        }
    }

    private fun observeViewModel(view: View) {
        mainViewModel.movieListState.observe(viewLifecycleOwner) { state ->
            with(binding) {
                when (state) {
                    is MovieListState.NoListState -> {
                        progressBarFragmentList.visibility = View.GONE
                        floatingActionButtonUpList.visibility = View.GONE
                        txtEmptyList.visibility = View.VISIBLE
                        recyclerViewMoviesList.visibility = View.GONE
                        toolBarFragmentList.visibility = View.GONE
                    }
                    is MovieListState.UpdatingState -> {
                        progressBarFragmentList.visibility = View.VISIBLE
                        floatingActionButtonUpList.visibility = View.GONE
                        txtEmptyList.visibility = View.GONE
                        recyclerViewMoviesList.visibility = View.GONE
                        toolBarFragmentList.visibility = View.GONE
                    }
                    is MovieListState.UpdatedState -> {
                        progressBarFragmentList.visibility = View.GONE
                        floatingActionButtonUpList.visibility = View.VISIBLE
                        txtEmptyList.visibility = View.GONE
                        recyclerViewMoviesList.visibility = View.VISIBLE
                        toolBarFragmentList.visibility = View.VISIBLE
                    }
                    is MovieListState.ErrorState -> {
                        progressBarFragmentList.visibility = View.GONE
                        Snackbar.make(view, state.message, Snackbar.LENGTH_SHORT).show()
                    }
                    else -> {
                        throw RuntimeException("Unknown state")
                    }
                }
            }
        }

        mainViewModel.movieList.observe(viewLifecycleOwner) { movieListAdapter.submitList(it) }
    }

    private fun setMoviePreviewOnClickListener() {
        movieListAdapter.onMovieItemClickListener = { _, kinopoiskId ->

            requireActivity().supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out
                )
                replace(
                    R.id.fragment_container,
                    MovieItemFragment.getNewInstance(kinopoiskId)
                )
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }

    private fun setupButtons() {
        binding.floatingActionButtonUpList.setOnClickListener {
            linearLayoutManager.scrollToPosition(SCROLL_TO_POSITION_VALUE)
        }
    }

    private fun setupSwipeRefreshLayout(view: View) {
        with(binding) {
            swipeRefreshLayout.setOnRefreshListener {
                mainViewModel.onRefreshList()
                Snackbar.make(view, UPDATED_MESSAGE, Snackbar.LENGTH_SHORT).show()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    companion object {
        private const val SCROLL_TO_POSITION_VALUE = 0
        private const val UPDATED_MESSAGE = "Movie list updated"

        fun getNewInstance(): MovieListFragment {
            return MovieListFragment()
        }
    }
}
