package com.example.proj.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.proj.CryptoUiState
import com.example.proj.CryptoViewModel
import com.example.proj.data.network.CryptoDto

@Composable
fun Home(viewModel: CryptoViewModel, onCryptoClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is CryptoUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is CryptoUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${state.message}", color = Color.Red)
            }
        }
        is CryptoUiState.Success -> {
            LazyColumn(
                    modifier = modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.coins) { coin ->
                    val isFavorite = state.favorites.any { it.id == coin.id }
                    CoinCard(
                            coin = coin,
                            isFavorite = isFavorite,
                            onFavoriteClick = { viewModel.toggleFavorite(coin) },
                            onClick = { onCryptoClick(coin.id) }
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CoinCard(
        coin: CryptoDto,
        isFavorite: Boolean,
        onFavoriteClick: () -> Unit,
        onClick: () -> Unit
) {
    Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                    Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { onClick() }
                            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                    model = "https://s2.coinmarketcap.com/static/img/coins/64x64/${coin.id}.png",
                    contentDescription = coin.name,
                    modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = coin.symbol, fontWeight = FontWeight.Bold)
                Text(text = coin.name, style = MaterialTheme.typography.bodySmall)
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                    text = "$${String.format("%.2f", coin.quote["USD"]?.price ?: 0.0)}",
                    fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onFavoriteClick) {
                Icon(
                        imageVector =
                                if (isFavorite) Icons.Filled.Favorite
                                else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray
                )
            }
        }
    }
}