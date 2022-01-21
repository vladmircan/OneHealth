@file:Suppress("FunctionName")

package com.example.onehealth.app.navigation

import android.app.Activity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.example.onehealth.app.OneHealthActivity
import com.example.onehealth.app.add_measurement.AddMeasurementScreen
import com.example.onehealth.app.add_measurement.AddMeasurementViewModel
import com.example.onehealth.app.auth.LoginScreen
import com.example.onehealth.app.auth.RegisterScreen
import com.example.onehealth.app.main.HomeScreen
import com.example.onehealth.app.view_measurements.ViewMeasurementsScreen
import com.example.onehealth.app.view_measurements.ViewMeasurementsViewModel
import com.example.onehealth.domain.model.local.MeasurementType
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope

@ExperimentalMaterialApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    coroutineScope: CoroutineScope,
    startDestination: Screen
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.baseRoute
    ) {
        composable(route = Screen.Home.baseRoute) {
            HomeScreen(hiltViewModel(), navController, coroutineScope)
        }
        composable(
            route = Screen.AddMeasurement.baseRoute,
            arguments = listOf(navArgument(Screen.AddMeasurement.ARG_MEASUREMENT_TYPE_NAME) {
                type = NavType.StringType
            })
        ) {
            val measurementTypeName =
                it.arguments?.getString(Screen.AddMeasurement.ARG_MEASUREMENT_TYPE_NAME)
            AddMeasurementScreen(
                addMeasurementViewModel(
                    measurementType = MeasurementType.valueOf(measurementTypeName!!)
                ),
                navController
            )
        }
        composable(
            route = Screen.ViewMeasurements.baseRoute,
            arguments = listOf(navArgument(Screen.ViewMeasurements.ARG_MEASUREMENT_TYPE_NAME) {
                type = NavType.StringType
            })
        ) {
            val measurementTypeName =
                it.arguments?.getString(Screen.ViewMeasurements.ARG_MEASUREMENT_TYPE_NAME)
            ViewMeasurementsScreen(
                viewMeasurementsViewModel(
                    measurementType = MeasurementType.valueOf(measurementTypeName!!)
                )
            )
        }
        composable(route = Screen.Login.baseRoute) {
            LoginScreen(hiltViewModel(), navController)
        }
        composable(route = Screen.Register.baseRoute) {
            RegisterScreen(hiltViewModel(), navController)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun addMeasurementViewModel(measurementType: MeasurementType): AddMeasurementViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        OneHealthActivity.AddMeasurementViewModelFactoryProvider::class.java
    ).addMeasurementViewModel()

    return viewModel(factory = AddMeasurementViewModel.provideFactory(factory, measurementType))
}

@ExperimentalMaterialApi
@Composable
fun viewMeasurementsViewModel(measurementType: MeasurementType): ViewMeasurementsViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        OneHealthActivity.ViewMeasurementsViewModelFactoryProvider::class.java
    ).viewMeasurementsViewModel()

    return viewModel(factory = ViewMeasurementsViewModel.provideFactory(factory, measurementType))
}