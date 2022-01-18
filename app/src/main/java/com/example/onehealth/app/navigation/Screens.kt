package com.example.onehealth.app.navigation

sealed class Screen(val route: String) {
    object Home: Screen(route = "home_screen")
    object AddMeasurement: Screen(route = "add_measurement")
    object ViewMeasurements: Screen(route = "view_measurements")
    object Login: Screen(route = "login_screen")
    object Register: Screen(route = "register_screen")
}