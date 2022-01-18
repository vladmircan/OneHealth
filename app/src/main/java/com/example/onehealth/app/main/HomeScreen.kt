package com.example.onehealth.app.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
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
                OverviewChart(viewModel.chartDataOfMeasurementType(measurementType))
            }
        }
    }
}

@Composable
fun AddMeasurementButton(
    onClick: () -> Unit
) {
    FloatingActionButton(onClick = onClick) {

    }
}

@Composable
fun OverviewChart(
    dataSource: Flow<ChartDataModel>
) {

    val chartData = dataSource.collectAsState(null)

    if (chartData.value == null)
        return
    AndroidView(
        modifier = Modifier.fillMaxWidth(fraction = 0.9f),
        factory = { context ->
            // Inside the viewBlock we create a good ol' fashion TextView to match the width and height of its
            // parent
            ChartCard(context, chartData.value!!) {

            }
        })

//    val data = listOf(
//        LineChartData.SeriesData(
//            points = chartData.value?.values?.flatMapIndexed { index, measurement ->
//                listOf(
//                    LineChartData.SeriesData.Point(index, measurement.value.toFloat()),
//                    LineChartData.SeriesData.Point(index + 1, 34f),
//                    LineChartData.SeriesData.Point(index + 2, 56f),
//                    LineChartData.SeriesData.Point(index + 3, 67f)
//                )
//            } ?: emptyList(),
//            color = Purple500,
//            title = stringResource(chartData.value?.chartLabelId ?: R.string.empty)
//        ))
//    LineChart(
//        chartHeight = 200.dp,
//        data = LineChartData(
//            series = data,
//        )
//    )
}