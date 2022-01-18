package com.example.onehealth.app.utils

import com.example.onehealth.R
import com.example.onehealth.domain.model.local.MeasurementType

val MeasurementType.labelStringId: Int
    get() {
        return when (this) {
            MeasurementType.BODY_WEIGHT -> R.string.body_weight
            MeasurementType.BODY_FAT_PERCENTAGE -> R.string.body_fat_percentage
        }
    }

val MeasurementType.unitCodeStringId: Int
    get() {
        return when (this) {
            MeasurementType.BODY_WEIGHT -> R.string.body_weight_unit_code
            MeasurementType.BODY_FAT_PERCENTAGE -> R.string.body_fat_percentage_unit_code
        }
    }