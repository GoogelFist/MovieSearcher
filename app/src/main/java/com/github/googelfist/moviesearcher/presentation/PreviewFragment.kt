package com.github.googelfist.moviesearcher.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.googelfist.moviesearcher.component
import com.github.googelfist.moviesearcher.databinding.FragmentPreviewBinding
import com.github.googelfist.moviesearcher.presentation.recycler.MoviesPreviewAdapter
import javax.inject.Inject

class PreviewFragment : Fragment() {

    @Inject
    lateinit var mainViewModelFabric: MainViewModelFabric

    lateinit var moviesPreviewAdapter: MoviesPreviewAdapter

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

        setupRecyclerView()

        mainViewModel.movieList.observe(viewLifecycleOwner) { moviesPreviewAdapter.submitList(it) }

        binding.bLoad.setOnClickListener {
            mainViewModel.onLoadFirstPageTop250BestFilms()
        }
    }

    private fun setupRecyclerView() {
        val rvMoviesPreview = binding.rvMoviesPreview

        moviesPreviewAdapter = MoviesPreviewAdapter()
        rvMoviesPreview.adapter = moviesPreviewAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun getNewInstance(): PreviewFragment {
            return PreviewFragment()
        }
    }
}