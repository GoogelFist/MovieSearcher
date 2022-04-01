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
import com.github.googelfist.moviesearcher.presentation.recycler.MoviesPreviewAdapter
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MovieListFragment : Fragment() {

    @Inject
    lateinit var mainViewModelFabric: MainViewModelFabric

    lateinit var moviesPreviewAdapter: MoviesPreviewAdapter
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
        movieListInit()
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
        val rvMoviesPreview = binding.rvMoviesList

        linearLayoutManager = LinearLayoutManager(requireActivity())
        rvMoviesPreview.layoutManager = linearLayoutManager

        moviesPreviewAdapter = MoviesPreviewAdapter()
        rvMoviesPreview.adapter = moviesPreviewAdapter

        moviesPreviewAdapter.onScrolledToBottomListener = {
            mainViewModel.onUpdateMovieList()
        }
    }

    private fun observeViewModel(view: View) {
        mainViewModel.movieList.observe(viewLifecycleOwner) {

            if (it.isEmpty()) {
                mainViewModel.onUpdateMovieList()
            } else {
                moviesPreviewAdapter.submitList(it)
            }
        }

        mainViewModel.loading.observe(viewLifecycleOwner) { value ->
            value.let { show ->
                binding.pbFragmentList.visibility = if (show) View.VISIBLE else View.GONE
            }
        }

        mainViewModel.snackBar.observe(viewLifecycleOwner) {
            it?.let {
                Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
                mainViewModel.onSnackBarShown()
            }
        }
    }

    private fun movieListInit() {
        mainViewModel.onLoadMovieList()
    }

    private fun setMoviePreviewOnClickListener() {
        moviesPreviewAdapter.onMovieItemClickListener = { _, kinopoiskId ->

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
        binding.fabUpList.setOnClickListener {
            linearLayoutManager.scrollToPosition(SCROLL_TO_POSITION_VALUE)
        }
    }

    private fun setupSwipeRefreshLayout(view: View) {
        binding.swipeRefreshLayout.setOnRefreshListener {
            mainViewModel.onUpdateMovieList()
            Snackbar.make(view, UPDATED_MESSAGE, Snackbar.LENGTH_SHORT).show()
            binding.swipeRefreshLayout.isRefreshing = false
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
