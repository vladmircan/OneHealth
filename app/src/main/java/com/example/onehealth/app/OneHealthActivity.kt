package com.example.onehealth.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.onehealth.app.auth.AuthViewModel
import com.example.onehealth.app.core.theme.OneHealthTheme
import com.example.onehealth.app.main.HomeViewModel
import com.example.onehealth.app.navigation.Screen
import com.example.onehealth.app.navigation.SetupNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OneHealthActivity: ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            this.setKeepVisibleCondition {
                when (val isLoggedIn = homeViewModel.isLoggedIn) {
                    null -> true
                    else -> {
                        showAppropriateScreen(isLoggedIn)
                        false
                    }
                }
            }
        }
    }

    private fun showAppropriateScreen(isLoggedIn: Boolean) {
        setContent {
            OneHealthTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    SetupNavGraph(
                        navController = navController,
                        startDestination = if (isLoggedIn)
                            Screen.Home
                        else
                            Screen.Login
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OneHealthTheme {
        Greeting("Android")
    }
}