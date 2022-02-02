package com.github.googelfist.moviesearcher.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.googelfist.moviesearcher.data.datasourse.network.model.ResponseMovieDTO
import com.github.googelfist.moviesearcher.domain.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: Repository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val movieList = MutableLiveData<ResponseMovieDTO>()
    var job: Job? = null

    val loading = MutableLiveData<Boolean>()

    fun loadTop250BestFilms(page: Int) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = repository.loadTop250BestFilms(page)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    movieList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
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