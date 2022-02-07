package com.github.googelfist.moviesearcher.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.googelfist.moviesearcher.R
import com.github.googelfist.moviesearcher.component
import com.github.googelfist.moviesearcher.databinding.FragmentPreviewBinding
import com.github.googelfist.moviesearcher.presentation.recycler.MoviesPreviewAdapter
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class PreviewFragment : Fragment() {

    @Inject
    lateinit var mainViewModelFabric: MainViewModelFabric

    lateinit var moviesPreviewAdapter: MoviesPreviewAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    private var _binding: FragmentPreviewBinding? = null
    private val binding: FragmentPreviewBinding
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.movieList.observe(viewLifecycleOwner) { moviesPreviewAdapter.submitList(it) }
        mainViewModel.loading.observe(viewLifecycleOwner) {
            binding.pbFragmentPreview.isVisible = it
        }
        mainViewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show() }
        }
        if (savedInstanceState == null) {
            mainViewModel.onLoadFirstPageTop250BestFilms()
        }

        setupRecyclerView()

        moviesPreviewAdapter.onMoviePreviewClickListener = {
            moviesPreviewAdapter.getMoviePreviewId = { kinopoiskId ->
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
                    .replace(
                        R.id.fragment_container,
                        DetailFragment.getNewInstanceWithId(kinopoiskId)
                    )
                    .commit()
            }
        }

        binding.fabLoad.setOnClickListener {
            linearLayoutManager.scrollToPosition(SCROLL_TO_POSITION_VALUE)
        }
    }

    private fun setupRecyclerView() {
        val rvMoviesPreview = binding.rvMoviesPreview

        linearLayoutManager = LinearLayoutManager(requireContext())
        rvMoviesPreview.layoutManager = linearLayoutManager

        moviesPreviewAdapter = MoviesPreviewAdapter()
        rvMoviesPreview.adapter = moviesPreviewAdapter

        rvMoviesPreview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val preloadPosition = moviesPreviewAdapter.itemCount - PRELOAD_POSITION

                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == preloadPosition) {
                    mainViewModel.onLoadNextPageTop250BestFilms()

                    val lastPosition = moviesPreviewAdapter.itemCount - ONE_VALUE

                    recyclerView.post {
                        moviesPreviewAdapter.notifyItemRangeChanged(lastPosition, ITEM_COUNT)
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val PRELOAD_POSITION = 3
        private const val ITEM_COUNT = 20
        private const val SCROLL_TO_POSITION_VALUE = 0
        private const val ONE_VALUE = 1

        fun getNewInstance(): PreviewFragment {
            return PreviewFragment()
        }
    }
}