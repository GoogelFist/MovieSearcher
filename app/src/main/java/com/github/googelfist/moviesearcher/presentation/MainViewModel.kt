package com.github.googelfist.moviesearcher.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.googelfist.moviesearcher.data.datasourse.network.model.MovieDTO
import com.github.googelfist.moviesearcher.domain.LoadTop250BestFilmsUseCase
import com.github.googelfist.moviesearcher.domain.Repository
import com.github.googelfist.moviesearcher.domain.model.MoviePreview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val loadTop250BestFilmsUseCase: LoadTop250BestFilmsUseCase) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val movieList = MutableLiveData<List<MoviePreview>>()
    var job: Job? = null

    val loading = MutableLiveData<Boolean>()

    fun onLoadTop250BestFilms(page: Int) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val moviesContainer = loadTop250BestFilmsUseCase(page)
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