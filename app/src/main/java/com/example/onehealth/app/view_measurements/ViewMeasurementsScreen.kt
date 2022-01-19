package com.example.onehealth.app.view_measurements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.onehealth.R
import com.example.onehealth.app.chart.ChartView
import com.example.onehealth.domain.model.local.ChartDataModel
import com.example.onehealth.domain.model.local.FormattedPeriod
import kotlinx.coroutines.flow.Flow

@Composable
fun ViewMeasurementsScreen(
    viewModel: ViewMeasurementsViewModel,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        WeekNavigation(viewModel)

        Chart(
            dataSource = viewModel.chartDataOfTimePeriod
        )
    }
}

@Composable
fun Chart(dataSource: Flow<ChartDataModel>) {

    val chartData = dataSource.collectAsState(null)
    if (chartData.value == null)
        return

    AndroidView(
        modifier = Modifier.fillMaxWidth(fraction = 0.95f).fillMaxHeight(fraction = 0.7f),
        factory = { context ->
            ChartView(context, chartData.value!!.chartLabelId).apply {
                measurementValues = chartData.value!!.values
            }
        },
        update = { chart ->
            chart.measurementValues = chartData.value!!.values
        }
    )
}

@Composable
fun WeekNavigation(viewModel: ViewMeasurementsViewModel) {
    val formattedPeriod = viewModel.formattedPeriod.collectAsState(
        initial = FormattedPeriod(startTime = "", endTime = "")
    )
    val canNavigateForwards = viewModel.canNavigateForwardsInTime.collectAsState(false)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { viewModel.navigateBackwardsInTime() }) {
            Icon(
                painter = painterResource(R.drawable.ic_chevron_left),
                contentDescription = "Back button"
            )
        }
        Text(text = "${formattedPeriod.value.startTime} - ${formattedPeriod.value.endTime}")
        IconButton(
            modifier = Modifier
                .alpha(if (canNavigateForwards.value) 1.0f else 0f),
            onClick = { viewModel.navigateForwardsInTime() },
            enabled = canNavigateForwards.value
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = "Forward button"
            )
        }
    }
}