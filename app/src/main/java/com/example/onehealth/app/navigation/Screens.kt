package com.example.onehealth.app.navigation

interface AuthScreen
sealed interface MainScreen {
    interface HomeScreen: MainScreen
}

sealed class Screen(val route: String) {
    object Home: Screen(route = "home_screen"), MainScreen.HomeScreen
    object AddMeasurement: Screen(route = "add_measurement"), MainScreen
    object ViewMeasurements: Screen(route = "view_measurements"), MainScreen
    object Login: Screen(route = "login_screen"), AuthScreen
    object Register: Screen(route = "register_screen"), AuthScreen
}