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
        requireActivity().component.inject(this)
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
        movieListInit()
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
            mainViewModel.onLoadMovieList()
        }
    }


    private fun observeViewModel(view: View) {
        mainViewModel.movieList.observe(viewLifecycleOwner) { moviesPreviewAdapter.submitList(it) }

        mainViewModel.loading.observe(viewLifecycleOwner) { value ->
            value.let { show ->
                binding.pbFragmentList.visibility = if (show) View.VISIBLE else View.GONE
            }
        }

        mainViewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let { Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show() }
        }
    }

    private fun movieListInit() {
        if (mainViewModel.movieList.value == null) {
            mainViewModel.onLoadMovieList()
        }
    }

    private fun setMoviePreviewOnClickListener() {
        moviesPreviewAdapter.onMovieItemClickListener = { _, kinopoiskId ->
            mainViewModel.onLoadMovieItem(kinopoiskId)

            requireActivity().supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out
                )
                replace(
                    R.id.fragment_container,
                    MovieItemFragment.getNewInstance()
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

    companion object {
        private const val SCROLL_TO_POSITION_VALUE = 0

        fun getNewInstance(): MovieListFragment {
            return MovieListFragment()
        }
    }
}
