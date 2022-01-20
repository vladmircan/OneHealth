package com.example.onehealth.app.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.onehealth.R
import com.example.onehealth.app.chart.ChartCard
import com.example.onehealth.app.core.components.GenericProgressIndicator
import com.example.onehealth.app.core.theme.Dimensions
import com.example.onehealth.app.navigation.Screen
import com.example.onehealth.app.utils.labelStringId
import com.example.onehealth.domain.model.local.ChartDataModel
import com.example.onehealth.domain.model.local.MeasurementType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController,
    coroutineScope: CoroutineScope
) {
    val isLoggedIn = viewModel.isLoggedIn.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState(false)
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val openBottomSheet = {
        coroutineScope.launch {
            bottomSheetScaffoldState.bottomSheetState.expand()
        }
    }

    if (isLoggedIn.value != true)
        navController.navigate(Screen.Login.baseRoute)


    GenericProgressIndicator(isLoadingState = isLoading)

    BottomSheetScaffold(
        drawerShape = MaterialTheme.shapes.medium,
        scaffoldState = bottomSheetScaffoldState,
        sheetElevation = 8.dp,
        sheetContent = {
            ChooseMesurementTypeBottomSheetContent(
                navController
            )
        },
        sheetPeekHeight = 0.dp
    ) {
        Scaffold(
            floatingActionButton = {
                AddMeasurementFloatingButton(onClick = { openBottomSheet() })
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
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
}

@Composable
fun AddMeasurementFloatingButton(
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
                navController.navigate(Screen.ViewMeasurements.getRoute(measurementType))
            }
        },
        update = { chart ->
            chart.measurementValues = chartData.value!!.values
        }
    )
}

@Composable
fun ChooseMesurementTypeBottomSheetContent(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp),
    ) {
        MeasurementType.values().forEach {
            MeasurementTypeItem(
                navController = navController,
                measurementType = it
            )
        }
    }
}

@Composable
fun MeasurementTypeItem(
    navController: NavController,
    measurementType: MeasurementType
) {
    Button(
        onClick = { navController.navigate(Screen.AddMeasurement.getRoute(measurementType)) }
    ) {
        Text(
            text = stringResource(measurementType.labelStringId)
        )
    }
}