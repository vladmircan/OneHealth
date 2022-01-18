package com.example.onehealth.app.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.onehealth.app.add_measurement.AddMeasurementScreen
import com.example.onehealth.app.auth.LoginScreen
import com.example.onehealth.app.auth.RegisterScreen
import com.example.onehealth.app.main.HomeScreen
import com.example.onehealth.app.view_measurements.ViewMeasurementsScreen
import com.example.onehealth.domain.model.local.MeasurementType

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: Screen
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(hiltViewModel(), navController)
        }
        composable(route = Screen.AddMeasurement.route) {
            AddMeasurementScreen(hiltViewModel(), navController)
        }
        composable(route = Screen.ViewMeasurements.route) {
            ViewMeasurementsScreen(hiltViewModel(), navController, MeasurementType.BODY_WEIGHT)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(hiltViewModel(), navController)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(hiltViewModel(), navController)
        }
    }
}