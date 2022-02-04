package com.github.googelfist.moviesearcher.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.googelfist.moviesearcher.component
import com.github.googelfist.moviesearcher.databinding.FragmentPreviewBinding
import com.github.googelfist.moviesearcher.presentation.recycler.MoviesPreviewAdapter
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

        setupRecyclerView()

        binding.bLoad.setOnClickListener {
            mainViewModel.onLoadFirstPageTop250BestFilms()
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
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == moviesPreviewAdapter.itemCount - PRELOAD_POSITION) {
                    mainViewModel.onLoadNextPageTop250BestFilms()
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

        fun getNewInstance(): PreviewFragment {
            return PreviewFragment()
        }
    }
}