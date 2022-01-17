package com.example.onehealth.domain.model.local

data class ChartDataModel(
    val chartLabelId: Int,
    val values: List<MeasurementModel>
)