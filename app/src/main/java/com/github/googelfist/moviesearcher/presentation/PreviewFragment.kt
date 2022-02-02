package com.github.googelfist.moviesearcher.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.googelfist.moviesearcher.R
import com.github.googelfist.moviesearcher.component
import com.google.android.material.button.MaterialButton
import javax.inject.Inject

class PreviewFragment: Fragment() {

    @Inject
    lateinit var mainViewModelFabric: MainViewModelFabric

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
    ): View? {
        return inflater.inflate(R.layout.fragment_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(R.id.b_load).setOnClickListener {
            mainViewModel.onLoadFirstPageTop250BestFilms()
        }
    }

    companion object {
        fun getNewInstance(): PreviewFragment {
            return PreviewFragment()
        }
    }
}