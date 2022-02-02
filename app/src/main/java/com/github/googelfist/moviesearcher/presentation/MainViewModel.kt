package com.github.googelfist.moviesearcher.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.googelfist.moviesearcher.domain.LoadFirstPageTop250UseCase
import com.github.googelfist.moviesearcher.domain.model.MoviePreview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val loadFirstPageTop250UseCase: LoadFirstPageTop250UseCase) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val movieList = MutableLiveData<List<MoviePreview>>()
    var job: Job? = null

    val loading = MutableLiveData<Boolean>()

    fun onLoadFirstPageTop250BestFilms() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val moviesContainer = loadFirstPageTop250UseCase()
            withContext(Dispatchers.Main) {
                if (moviesContainer.previewMovies.isNotEmpty()) {
                    movieList.postValue(moviesContainer.previewMovies)
                    loading.value = false
                } else {
                    onError(moviesContainer.errorMessage)
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}