package com.example.proj

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proj.data.CryptoRepository
import com.example.proj.data.local.CryptoEntity
import com.example.proj.data.network.CryptoDto
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface CryptoUiState {
    data object Loading : CryptoUiState
    data class Success(val coins: List<CryptoDto>, val favorites: List<CryptoEntity>) : CryptoUiState
    data class Error(val message: String) : CryptoUiState
}

class CryptoViewModel(private val repository: CryptoRepository) : ViewModel() {

    private val favorites =
            repository.favorites.stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5000),
                    emptyList()
            )

    private val _coins = kotlinx.coroutines.flow.MutableStateFlow<List<CryptoDto>>(emptyList())
    private val _uiState =
            kotlinx.coroutines.flow.MutableStateFlow<CryptoUiState>(CryptoUiState.Loading)
    val uiState: StateFlow<CryptoUiState> = _uiState

    init {
        fetchCoins()
    }

    private fun fetchCoins() {
        viewModelScope.launch {
            _uiState.value = CryptoUiState.Loading
            repository
                    .getCoins()
                    .fold(
                            onSuccess = { coins ->
                                _coins.value = coins
                                updateUiState(coins, favorites.value)
                            },
                            onFailure = {
                                _uiState.value = CryptoUiState.Error(it.message ?: "Unknown Error")
                            }
                    )
        }

        viewModelScope.launch {
            favorites.collect { favs ->
                if (_coins.value.isNotEmpty()) {
                    updateUiState(_coins.value, favs)
                }
            }
        }
    }

    private fun updateUiState(coins: List<CryptoDto>, favs: List<CryptoEntity>) {
        _uiState.value = CryptoUiState.Success(coins, favs)
    }

    fun toggleFavorite(coin: CryptoDto) {
        viewModelScope.launch {
            val price = coin.quote["USD"]?.price ?: 0.0
            repository.toggleFavorite(coin.id, coin.name, coin.symbol, price)
        }
    }

    fun getCryptoById(id: Int): CryptoDto? {
        return _coins.value.find { it.id == id }
    }
}

class CryptoViewModelFactory(private val repository: CryptoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CryptoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return CryptoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
