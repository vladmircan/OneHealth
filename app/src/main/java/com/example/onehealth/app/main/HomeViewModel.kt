package com.example.onehealth.app.main

import androidx.lifecycle.viewModelScope
import com.example.onehealth.app.core.BaseViewModel
import com.example.onehealth.app.utils.labelStringId
import com.example.onehealth.domain.core.AppDispatchers
import com.example.onehealth.domain.core.UseCase
import com.example.onehealth.domain.model.local.ChartDataModel
import com.example.onehealth.domain.model.local.MeasurementType
import com.example.onehealth.domain.repository.UserRepository
import com.example.onehealth.domain.use_case.FlowLastMeasurementsOfMeasurementTypeUseCase
import com.example.onehealth.domain.use_case.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val flowLastMeasurementsOfMeasurementTypeUseCase: FlowLastMeasurementsOfMeasurementTypeUseCase,
    userRepository: UserRepository,
    private val logoutUseCase: LogoutUseCase
): BaseViewModel() {

    val isLoggedIn: StateFlow<Boolean?> = userRepository.isLoggedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )
    private var chartDataFlows: Map<MeasurementType, Flow<ChartDataModel>> = emptyMap()

    init {
        viewModelScope.launch(AppDispatchers.IO) {
            chartDataFlows = MeasurementType.values().associateWith { measurementType ->
                flowChartDataOfMeasurementType(measurementType)
            }
        }
    }

    fun chartDataOfMeasurementType(measurementType: MeasurementType): Flow<ChartDataModel> {
        return chartDataFlows[measurementType]!!
    }

    fun logout() {
        logoutUseCase.perform(UseCase.NoParams)
    }

    private suspend fun flowChartDataOfMeasurementType(measurementType: MeasurementType): Flow<ChartDataModel> {
        val params = FlowLastMeasurementsOfMeasurementTypeUseCase.Params(measurementType)
        return flowLastMeasurementsOfMeasurementTypeUseCase(params)
            .getOrDefault(flow { emit(emptyList()) })
            .map { values -> ChartDataModel(measurementType.labelStringId, values) }
    }
}