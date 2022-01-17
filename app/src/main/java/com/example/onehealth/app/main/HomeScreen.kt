package com.example.onehealth.app.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.onehealth.R
import com.example.onehealth.app.core.theme.Dimensions
import com.example.onehealth.app.core.theme.Purple500
import com.example.onehealth.app.navigation.Screen
import com.example.onehealth.domain.model.local.ChartDataModel
import com.example.onehealth.domain.model.local.MeasurementType
import hu.ma.charts.line.LineChart
import hu.ma.charts.line.data.LineChartData
import kotlinx.coroutines.flow.Flow

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimensions.ChartSpacing)
    ) {

        val measurementType = MeasurementType.BODY_WEIGHT
//        MeasurementType.values().forEach { measurementType ->
        OverviewChart(viewModel.chartDataOfMeasurementType(measurementType), measurementType)
//        }

        AddMeasurementButton {
            navController.navigate(Screen.AddMeasurement.route)
        }
    }
}

@Composable
fun AddMeasurementButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick
    ) {

    }
}

@Composable
fun OverviewChart(
    dataSource: Flow<ChartDataModel>,
    measurementType: MeasurementType
) {

    val chartData = dataSource.collectAsState(null)

    if (chartData.value == null)
        return

    val data = listOf(
        LineChartData.SeriesData(
            points = chartData.value?.values?.mapIndexed { index, measurement ->
                LineChartData.SeriesData.Point(index, measurement.value.toFloat())
            } ?: emptyList(),
            color = Purple500,
            title = stringResource(chartData.value?.chartLabelId ?: R.string.empty)
        ))
    LineChart(
        chartHeight = 200.dp,
        data = LineChartData(
            series = data,
        )
    )
//    Box(modifier = Modifier.fillMaxSize(0.3f).background(Color.Blue))
//}
//    LineChart(
//        lineChartData = LineChartData(
//            points = chartData.value?.values?.mapIndexed { index, measurement ->
//                LineChartData.Point(measurement.value.toFloat(), measurement.timeStamp.toString())
//            } ?: emptyList()
//        ),
//        modifier = Modifier.fillMaxSize(),
//        animation = simpleChartAnimation(),
//        pointDrawer = FilledCircularPointDrawer(),
//        lineDrawer = SolidLineDrawer(),
//        xAxisDrawer = SimpleXAxisDrawer(),
//        yAxisDrawer = SimpleYAxisDrawer(),
//        horizontalOffset = 5f
//    )
}