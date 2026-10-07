package com.pemmob.yggdrasil.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.yggdrasil.data.model.Anime
import com.pemmob.yggdrasil.data.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailUiState(
    val isLoading: Boolean = true,
    val anime: Anime? = null,
    val error: String? = null
)

class DetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = AnimeRepository()

    private val malId: Int = checkNotNull(savedStateHandle["malId"])

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    fun loadDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.getAnimeDetail(malId)
                .onSuccess { anime ->
                    _uiState.update { it.copy(isLoading = false, anime = anime) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.toUserMessage()) }
                }
        }
    }
}
