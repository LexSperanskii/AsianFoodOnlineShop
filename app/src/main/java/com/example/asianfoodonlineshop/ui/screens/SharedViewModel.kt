package com.example.asianfoodonlineshop.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SharedViewModelUiState(
    val id: Int = 0
)

class SharedViewModel() : ViewModel() {

    private val _sharedViewModelUiState = MutableStateFlow(SharedViewModelUiState())
    val sharedViewModelUiState: StateFlow<SharedViewModelUiState> = _sharedViewModelUiState.asStateFlow()
    fun chooseCommodityItem(id: Int) {
        _sharedViewModelUiState.update {
            it.copy(
                id = id
            )
        }
    }
}