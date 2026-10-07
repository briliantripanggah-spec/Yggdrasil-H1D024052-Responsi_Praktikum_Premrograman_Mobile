package com.pemmob.yggdrasil.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.yggdrasil.data.model.Anime
import com.pemmob.yggdrasil.data.model.Genre
import com.pemmob.yggdrasil.data.repository.AnimeRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class HomeUiState(
    val query: String = "",
    val animeList: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val genres: List<Genre> = emptyList(),
    val selectedGenreId: Int? = null
)

class HomeViewModel : ViewModel() {

    private val repository = AnimeRepository()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadGenres()
        search()
    }

    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
        search(delayMs = 500)
    }

    fun onGenreSelected(genreId: Int?) {
        _uiState.update { it.copy(selectedGenreId = genreId) }
        search()
    }

    fun retry() {
        if (_uiState.value.genres.isEmpty()) loadGenres()
        search()
    }

    private fun search(delayMs: Long = 0) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (delayMs > 0) delay(delayMs)
            _uiState.update { it.copy(isLoading = true, error = null) }

            val state = _uiState.value
            repository.searchAnime(state.query, state.selectedGenreId)
                .onSuccess { list ->
                    _uiState.update { it.copy(animeList = list, isLoading = false) }
                }
                .onFailure { e ->
                    if (e is CancellationException) throw e
                    _uiState.update { it.copy(isLoading = false, error = e.toUserMessage()) }
                }
        }
    }

    private fun loadGenres() {
        viewModelScope.launch {
            repository.getGenres().onSuccess { genres ->
                _uiState.update { it.copy(genres = genres) }
            }
        }
    }
}

internal fun Throwable.toUserMessage(): String = when (this) {
    is HttpException ->
        if (code() == 429) "Terlalu banyak permintaan, tunggu sebentar lalu coba lagi."
        else "Server bermasalah (kode ${code()})."
    is IOException -> "Tidak ada koneksi internet."
    else -> "Terjadi kesalahan: ${message ?: "tidak diketahui"}"
}
