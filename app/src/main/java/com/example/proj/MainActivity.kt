package com.example.proj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.proj.data.CryptoRepository
import com.example.proj.data.local.AppDatabase
import com.example.proj.data.network.RetrofitInstance
import com.example.proj.ui.theme.ProjTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database =
                Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "crypto-db"
                ).build()

        val repository =
            CryptoRepository(
                RetrofitInstance.api,
                database.cryptoDao()
            )

        val factory = CryptoViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory)[CryptoViewModel::class.java]

        setContent { ProjTheme { CryptoApp(viewModel) } }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProjTheme {
        // CryptoApp() // Preview disabled: Requires ViewModel with Repository
    }
}
