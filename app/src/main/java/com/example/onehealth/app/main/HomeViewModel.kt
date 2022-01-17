package com.example.onehealth.app.main

import androidx.lifecycle.viewModelScope
import com.example.onehealth.app.core.BaseViewModel
import com.example.onehealth.app.utils.labelStringId
import com.example.onehealth.domain.core.AppDispatchers
import com.example.onehealth.domain.model.local.ChartDataModel
import com.example.onehealth.domain.model.local.MeasurementModel
import com.example.onehealth.domain.model.local.MeasurementType
import com.example.onehealth.domain.repository.UserRepository
import com.example.onehealth.domain.use_case.FlowLastMeasurementsOfMeasurementTypeUseCase
import com.example.onehealth.domain.utils.collectInScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val flowLastMeasurementsOfMeasurementTypeUseCase: FlowLastMeasurementsOfMeasurementTypeUseCase,
    userRepository: UserRepository
): BaseViewModel() {

    var isLoggedIn: Boolean? = null
    private var chartDataFlows: Map<MeasurementType, Flow<ChartDataModel>> = emptyMap()

    init {
        viewModelScope.launch(AppDispatchers.IO) {
            userRepository.isLoggedIn.collectInScope(
                viewModelScope,
                AppDispatchers.IO
            ) { isLoggedIn ->
                this@HomeViewModel.isLoggedIn = isLoggedIn
            }
            chartDataFlows = MeasurementType.values().associateWith { measurementType ->
                flowChartDataOfMeasurementType(measurementType)
            }
        }
    }

    fun chartDataOfMeasurementType(measurementType: MeasurementType): Flow<ChartDataModel> {
        return chartDataFlows[measurementType]!!
    }

    private suspend fun flowChartDataOfMeasurementType(measurementType: MeasurementType): Flow<ChartDataModel> {
        val params = FlowLastMeasurementsOfMeasurementTypeUseCase.Params(measurementType)
        return flowLastMeasurementsOfMeasurementTypeUseCase(params)
            .getOrDefault(flow { emit(emptyList<MeasurementModel>()) })
            .map { values -> ChartDataModel(measurementType.labelStringId, values) }
    }
}