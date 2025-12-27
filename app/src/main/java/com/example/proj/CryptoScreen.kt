package com.example.proj

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proj.screens.DetailScreen
import com.example.proj.screens.Home

enum class CryptoScreen(@StringRes val title: Int) {
    Home(title = R.string.home),
    Favorite(title = R.string.favorite),
    Detail(title = R.string.detail),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoAppBar(
        currentScreen: CryptoScreen,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit,
        modifier: Modifier = Modifier
) {
    TopAppBar(
            title = {
                Text(
                        text = stringResource(currentScreen.title),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                )
            },
            colors =
                    TopAppBarDefaults.mediumTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
            modifier = modifier,
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp) {
                        Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            }
    )
}

@Composable
fun CryptoApp(
        viewModel: CryptoViewModel,
        navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val currentScreen =
            try {
                if (currentRoute?.startsWith("Detail") == true) {
                    CryptoScreen.Detail
                } else {
                    CryptoScreen.valueOf(currentRoute ?: CryptoScreen.Home.name)
                }
            } catch (e: IllegalArgumentException) {

                CryptoScreen.Home
            }

    Scaffold(
            topBar = {
                CryptoAppBar(
                        currentScreen = currentScreen,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() }
                )
            }
    ) { innerPadding ->
        NavHost(
                navController = navController,
                startDestination = CryptoScreen.Home.name,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            composable(route = CryptoScreen.Home.name) {
                Home(
                        viewModel = viewModel,
                        onCryptoClick = { id ->
                            navController.navigate("${CryptoScreen.Detail.name}/$id")
                        }
                )
            }

            composable(
                    route = "${CryptoScreen.Detail.name}/{cryptoId}",
                    arguments =
                            listOf(
                                navArgument("cryptoId") {
                                    type = NavType.IntType
                                }
                            )
            ) { backStackEntry ->
                val cryptoId = backStackEntry.arguments?.getInt("cryptoId") ?: 0
                DetailScreen(cryptoId = cryptoId, viewModel = viewModel)
            }
        }
    }
}
