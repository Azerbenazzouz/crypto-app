package com.example.proj.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.proj.CryptoUiState
import com.example.proj.CryptoViewModel

@SuppressLint("DefaultLocale")
@Composable
fun DetailScreen(cryptoId: Int, viewModel: CryptoViewModel, modifier: Modifier = Modifier) {
    val crypto = viewModel.getCryptoById(cryptoId)
    val uiState by viewModel.uiState.collectAsState()

    val isFavorite =
            (uiState as? CryptoUiState.Success)?.favorites?.any {
                it.id == cryptoId
            } == true

    if (crypto != null) {
        Column(
                modifier = modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
        ) {
            AsyncImage(
                    model = "https://s2.coinmarketcap.com/static/img/coins/64x64/${crypto.id}.png",
                    contentDescription = crypto.name,
                    modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                    text = crypto.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
            )
            Text(
                    text = crypto.symbol,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                    text = "$${String.format("%.2f", crypto.quote["USD"]?.price ?: 0.0)}",
                    style = MaterialTheme.typography.headlineLarge
            )


            Spacer(modifier = Modifier.height(16.dp))

            val context = LocalContext.current
            val price = crypto.quote["USD"]?.price ?: 0.0
            val formattedPrice = String.format("%.2f", price)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(
                    onClick = { viewModel.toggleFavorite(crypto) },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector =
                                if (isFavorite) Icons.Filled.Favorite
                                else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                }

                IconButton(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "${crypto.name} is at $$formattedPrice")
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share Coin Price")
                        context.startActivity(shareIntent)
                    },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    } else {
        Text("Crypto not found", modifier = Modifier.padding(16.dp))
    }
}
