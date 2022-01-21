package com.example.onehealth.app.view_measurements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.onehealth.R
import com.example.onehealth.app.core.BaseViewModel
import com.example.onehealth.app.utils.labelStringId
import com.example.onehealth.domain.model.local.ChartDataModel
import com.example.onehealth.domain.model.local.FormattedPeriod
import com.example.onehealth.domain.model.local.MeasurementType
import com.example.onehealth.domain.model.local.Period
import com.example.onehealth.domain.use_case.GetMeasurementsUseCase
import com.example.onehealth.domain.utils.DateFormats
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar

class ViewMeasurementsViewModel @AssistedInject constructor(
    @Assisted private val measurementType: MeasurementType,
    private val getMeasurementsUseCase: GetMeasurementsUseCase
): BaseViewModel() {

    private val _chartDataOfTimePeriod = MutableStateFlow(
        ChartDataModel(R.string.empty, emptyList())
    )
    val chartDataOfTimePeriod = _chartDataOfTimePeriod

    private val _canNavigateForwardsInTime = MutableStateFlow(false)
    val canNavigateForwardsInTime: Flow<Boolean> = _canNavigateForwardsInTime

    private val startCalendar = Calendar.getInstance().apply { jumpToBeginningOfWeek() }
    private val endCalendar = Calendar.getInstance().apply { jumpToEndOfWeek() }

    private val currentPeriod
        get() = Period(startCalendar.timeInMillis, endCalendar.timeInMillis)
    private val _formattedPeriod = MutableStateFlow(getFormattedPeriod())
    val formattedPeriod: Flow<FormattedPeriod> = _formattedPeriod

    init {
        refreshData()
    }

    fun navigateBackwardsInTime() {
        navigateInTimeBy(-1)
    }

    fun navigateForwardsInTime() {
        navigateInTimeBy(1)
    }

    private fun navigateInTimeBy(amount: Int) {
        endCalendar.add(TIME_WINDOW_CALENDAR_FIELD, amount)
        endCalendar.jumpToEndOfWeek()

        startCalendar.add(TIME_WINDOW_CALENDAR_FIELD, amount)

        _canNavigateForwardsInTime.value = endCalendar.timeInMillis < System.currentTimeMillis()
        refreshData()
    }

    private fun refreshData() {
        getMeasurementsUseCase.perform(
            input = GetMeasurementsUseCase.Params(
                measurementType = measurementType,
                period = currentPeriod
            ),
            onSuccess = { retrievedMeasurements ->
                _chartDataOfTimePeriod.value = ChartDataModel(
                    chartLabelId = measurementType.labelStringId,
                    values = retrievedMeasurements
                )
                _formattedPeriod.value = getFormattedPeriod()
            }
        )
    }

    private fun getFormattedPeriod() = currentPeriod.format(DATE_FORMAT)

    private fun Calendar.jumpToBeginningOfWeek() = apply {
        set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
    }

    private fun Calendar.jumpToEndOfWeek() = apply {
        set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        add(Calendar.DAY_OF_WEEK, NUMBER_OF_DAYS_IN_WEEK - 1)
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(measurementType: MeasurementType): ViewMeasurementsViewModel
    }

    companion object {
        const val NUMBER_OF_DAYS_IN_WEEK = 7
        const val TIME_WINDOW_CALENDAR_FIELD = Calendar.WEEK_OF_YEAR
        const val DATE_FORMAT = DateFormats.DATE_FORMAT_DD_MM_YYYY

        fun provideFactory(
            assistedFactory: AssistedFactory,
            measurementType: MeasurementType
        ): ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T: ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(measurementType) as T
            }
        }
    }
}