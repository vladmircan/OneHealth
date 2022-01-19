package com.example.onehealth.app.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.onehealth.R
import com.example.onehealth.app.chart.ChartCard
import com.example.onehealth.app.core.theme.Dimensions
import com.example.onehealth.app.navigation.Screen
import com.example.onehealth.domain.model.local.ChartDataModel
import com.example.onehealth.domain.model.local.MeasurementType
import kotlinx.coroutines.flow.Flow

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val isLoggedIn = viewModel.isLoggedIn.collectAsState()

    if (isLoggedIn.value != true)
        navController.navigate(Screen.Login.route)

    Scaffold(
        floatingActionButton = {
            AddMeasurementButton {
                navController.navigate(Screen.AddMeasurement.route)
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimensions.ChartSpacing)
        ) {
            items(items = MeasurementType.values()) { measurementType ->
                OverviewChart(
                    dataSource = viewModel.chartDataOfMeasurementType(measurementType),
                    measurementType = measurementType,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun AddMeasurementButton(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(25.dp),
            painter = painterResource(R.drawable.ic_plus),
            contentDescription = "Add Measurement",
            tint = Color.White
        )
    }
}

@Composable
fun OverviewChart(
    dataSource: Flow<ChartDataModel>,
    measurementType: MeasurementType,
    navController: NavController
) {
    val chartData = dataSource.collectAsState(null)

    if (chartData.value == null)
        return

    AndroidView(
        modifier = Modifier.fillMaxWidth(fraction = 0.9f),
        factory = { context ->
            ChartCard(context, chartData.value!!) {
                navController.navigate(Screen.ViewMeasurements.route)
            }
        },
        update = { chart ->
            chart.measurementValues = chartData.value!!.values
        }
    )
}